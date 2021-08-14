package ge.dadeishvili.messengerapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.fragments.MessageFragment.Companion.getNickName
import ge.dadeishvili.messengerapp.fragments.SignUp.Companion.chooseImage
import ge.dadeishvili.messengerapp.fragments.SignUp.Companion.uploadImage
import ge.dadeishvili.messengerapp.models.User


class Profile : Fragment() {
    private lateinit var toDoView: EditText
    private lateinit var nickNameView: TextView
    private lateinit var nickName: String
    private lateinit var updateButton: Button
    private lateinit var usersRef: DatabaseReference
    private lateinit var dialog: AlertDialog
    private lateinit var imageView: ImageView
    private lateinit var imageUri: Uri
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_page, container, false)
        init(view)
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        setProfile()
        imageView.setOnClickListener {
            chooseImage(this)
        }
        update()
        view.findViewById<Button>(R.id.profilePage_signOut_button).setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_profile_to_signIn)
        }

        view.findViewById<Button>(R.id.profilePage_home_button).setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_message)
        }

        view.findViewById<FloatingActionButton>(R.id.profilePage_fab_button).setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_search)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            imageView.setImageURI(imageUri)
        }
    }

    private fun init(view: View) {
        nickNameView = view.findViewById(R.id.profilePage_NameText)
        toDoView = view.findViewById(R.id.profilePage_toDo_text)
        updateButton = view.findViewById(R.id.profilePage_update_button)
        usersRef = Firebase.database.getReference(SignUp.USERS_DB)
        imageView = view.findViewById(R.id.profilePage_imageView)
        storageRef = Firebase.storage.reference
        nickName = getNickName()
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()
    }

    private fun setProfile() {
        nickNameView.text = nickName
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.child(nickName).value as HashMap<*, *>
                val todo = hashMap["todo"] as String
                toDoView.text = SpannableStringBuilder(todo)
                setImage(
                    storageRef,
                    nickNameView.text.toString(),
                    imageView,
                    dialog
                )
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })
    }

    private fun update() {
        updateButton.setOnClickListener {
            val user = User(nickName, toDoView.text.toString())
            usersRef.child(nickName).setValue(user).addOnCompleteListener {
                uploadImage(storageRef, imageView, requireContext(), nickNameView.text.toString())
                toDoView.clearFocus()
                Toast.makeText(context, "Data changed successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Data didn't change", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun setImage(
            storageRef: StorageReference,
            nickname: String,
            imageView: ImageView,
            dialog: AlertDialog,
        ) {
            val islandRef = storageRef.child("images/$nickname.jpg")
            val oneMegaByte: Long = 1024 * 1024
            islandRef.getBytes(oneMegaByte).addOnSuccessListener {
                val options = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size, options)
                imageView.setImageBitmap(bitmap)
                dialog.dismiss()
            }.addOnFailureListener {
                dialog.dismiss()
            }
        }
    }
}