package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User

class SignUp : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_up, container, false)
        val nick = view.findViewById<EditText>(R.id.enterNameText1)
        val password = view.findViewById<EditText>(R.id.enterPasswordText1)
        val todo = view.findViewById<EditText>(R.id.todoText1)
        val usersRef = Firebase.database.getReference("Users")
        view.findViewById<Button>(R.id.signUpButton1).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString()
                    .isEmpty() || todo.text.toString().isEmpty()
            ) {
                val snack = Snackbar.make(it, "Fill all the information!", Snackbar.LENGTH_LONG)
                snack.show()
                return@setOnClickListener
            }
            val user = User(
                nick.text.toString(),
                password.text.toString(),
                todo.text.toString()
            )
            usersRef.child(nick.text.toString()).setValue(user)
            val args = bundleOf("user" to user)
            findNavController().navigate(R.id.action_signUp_to_message, args)
        }
        return view
    }

}