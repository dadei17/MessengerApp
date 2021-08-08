package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R

class SignIn : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser != null) {
            findNavController().navigate(R.id.action_signIn_to_message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_in, container, false)
        val nick = view.findViewById<EditText>(R.id.sign_in_nick_text)
        val password = view.findViewById<EditText>(R.id.sign_in_password_text)
        view.findViewById<Button>(R.id.sign_in_signIn_button).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                Toast.makeText(
                    context, "Fill all the information!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            logIn(nick.text.toString(), password.text.toString())
        }
        view.findViewById<Button>(R.id.sign_in_signUp_button).setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }
        return view
    }

    private fun logIn(nick: String, password: String) {
        val email = nick.plus(EMAIL_ADD)
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_signIn_to_message)
                } else {
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val EMAIL_ADD = "@gmail.com"
    }
}