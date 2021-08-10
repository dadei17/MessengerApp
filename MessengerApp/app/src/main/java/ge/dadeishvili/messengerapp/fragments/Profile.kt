package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User


class Profile : Fragment() {
    private lateinit var toDoView: EditText
    private lateinit var nickNameView: TextView
    private lateinit var nickName: String
    private lateinit var updateButton: Button
    private lateinit var usersRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_page, container, false)
//        findNavController().navigate(R.id.action_profile_to_loading)
        init(view)
        setProfile()
//        findNavController().navigate(R.id.action_loading_to_profile)
        update()

        view.findViewById<Button>(R.id.profilePage_signOut_button).setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_profile_to_signIn)
        }

        return view
    }

    private fun init(view: View) {
        nickNameView = view.findViewById(R.id.profilePage_NameText)
        toDoView = view.findViewById(R.id.profilePage_toDo_text)
        updateButton = view.findViewById(R.id.profilePage_update_button)
        usersRef = Firebase.database.getReference(SignUp.USERS_DB)
        val email = Firebase.auth.currentUser!!.email
        nickName = email!!.substring(0, email.indexOf('@'))
    }

    private fun setProfile(){
        nickNameView.text = nickName
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.child(nickName).value as HashMap<*, *>
                val todo = hashMap["todo"] as String
                toDoView.text = SpannableStringBuilder(todo)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun update() {
        updateButton.setOnClickListener {
            val user = User(nickName, toDoView.text.toString())
            usersRef.child(nickName).setValue(user).addOnCompleteListener {
                toDoView.clearFocus()
                Toast.makeText(context, "Data changed successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Data didn't change", Toast.LENGTH_SHORT).show()
            }
        }
    }
}