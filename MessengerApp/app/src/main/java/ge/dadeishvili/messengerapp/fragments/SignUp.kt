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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User
import java.io.ByteArrayOutputStream
import java.io.IOException

class SignUp : Fragment() {
    private val PICK_IMAGE = 1
    private val baos = ByteArrayOutputStream()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
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
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        database = Firebase.database
        imageView.setOnClickListener {
            chooseImage()
        }
        view.findViewById<Button>(R.id.sign_up_signUp_button).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString()
                    .isEmpty() || todo.text.toString().isEmpty()
            ) {
                val snack = Snackbar.make(it, "Fill all the information!", Snackbar.LENGTH_LONG)
                snack.show()
                return@setOnClickListener
            }
            auth = Firebase.auth
            createUser(nick.text.toString(), password.text.toString(), todo.text.toString())
//            val args = bundleOf("user" to user)
        }
        return view
    }

    private fun createUser(nick: String, password: String, todo: String) {
        auth.createUserWithEmailAndPassword(nick, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(nick, password, todo)
                    val usersRef = database.getReference(USERS_DB)
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

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                val imageView: ImageView = activity?.findViewById(R.id.sign_up_imageView)!!
                imageView.setImageBitmap(bitmap)
                baos.reset()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val USERS_DB = "Users"
    }
}