package ge.dadeishvili.messengerapp.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User
import java.io.ByteArrayOutputStream

class SignUp : Fragment() {
    private lateinit var nick: EditText
    private lateinit var password: EditText
    private lateinit var todo: EditText
    private lateinit var imageView: ImageView
    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_up, container, false)
        init(view)
        imageView.setOnClickListener {
            chooseImage(this)
        }
        view.findViewById<Button>(R.id.sign_up_signUp_button).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString()
                    .isEmpty() || todo.text.toString().isEmpty()
            ) {
                Toast.makeText(context, "Fill all the information!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.length() < 6) {
                Toast.makeText(
                    context,
                    "Password should be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            createUser(nick.text.toString(), password.text.toString(), todo.text.toString())
        }
        return view
    }

    private fun init(view: View) {
        storage = Firebase.storage
        storageRef = storage.reference
        nick = view.findViewById(R.id.sign_up_nick_text)
        password = view.findViewById(R.id.sign_up_password_text)
        todo = view.findViewById(R.id.sign_up_toDo_text)
        imageView = view.findViewById(R.id.sign_up_imageView)
    }

    private fun createUser(nickName: String, password: String, todo: String) {
        val email = nickName.plus(EMAIL_ADD)
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(nickName, todo)
                    val usersRef = Firebase.database.getReference(USERS_DB)
                    usersRef.child(nickName).setValue(user)
                    uploadImage(storageRef, imageView, requireContext(), nick.text.toString())
                    findNavController().navigate(R.id.action_signUp_to_message)
                } else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            imageView.setImageURI(imageUri)
        }
    }

    companion object {
        const val USERS_DB = "Users"
        const val EMAIL_ADD = "@gmail.com"

        fun chooseImage(fragment: Fragment) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            fragment.startActivityForResult(intent, 1)
        }

        fun uploadImage(
            storageRef: StorageReference,
            imageView: ImageView,
            context: Context,
            nick: String
        ) {
            val imagesRef = storageRef.child("images/$nick.jpg")
//        imageView.isDrawingCacheEnabled = true
//        imageView.buildDrawingCache()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = imagesRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(context, "Photo upload failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}