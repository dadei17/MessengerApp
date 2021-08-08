package ge.dadeishvili.messengerapp.fragments


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User
import java.io.ByteArrayOutputStream
import java.io.IOException

class SignUp : Fragment() {
    private lateinit var nick: EditText
    private lateinit var password: EditText
    private lateinit var todo: EditText
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_up, container, false)
        nick = view.findViewById(R.id.sign_up_nick_text)
        password = view.findViewById(R.id.sign_up_password_text)
        todo = view.findViewById(R.id.sign_up_toDo_text)
        imageView = view.findViewById(R.id.sign_up_imageView)
        view.findViewById<Button>(R.id.sign_up_signUp_button).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString()
                    .isEmpty() || todo.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    context, "Fill all the information!",
                    Toast.LENGTH_SHORT
                ).show()
            }else if(password.length() < 6) {
                Toast.makeText(
                    context, "Password should be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            createUser(nick.text.toString(), password.text.toString(), todo.text.toString())
        }
        return view
    }

    private fun createUser(nick: String, password: String, todo: String) {
        val email = nick.plus(EMAIL_ADD)
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(nick, todo)
                    val usersRef = Firebase.database.getReference(USERS_DB)
                    usersRef.child(nick).setValue(user)
                    findNavController().navigate(R.id.action_signUp_to_message)
                } else {
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        const val USERS_DB = "Users"
        const val EMAIL_ADD = "@gmail.com"
    }
}