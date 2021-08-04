package ge.dadeishvili.messengerapp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ge.dadeishvili.messengerapp.R
import java.io.ByteArrayOutputStream
import java.io.IOException

class SignIn : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private val baos = ByteArrayOutputStream()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_in, container, false)
        val nick = view.findViewById<EditText>(R.id.enterNameText)
        val password = view.findViewById<EditText>(R.id.enterPasswordText)
        val imgView = view.findViewById<ImageView>(R.id.imageView)
        val bitmap = (imgView.drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        view.findViewById<Button>(R.id.signInButton).setOnClickListener {
            if (nick.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                val snack = Snackbar.make(it, "Fill all the information!", Snackbar.LENGTH_LONG)
                snack.show()
                return@setOnClickListener
            }
        }
        imgView.setOnClickListener {
            chooseImage()
        }
        return view
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                val imageView: ImageView = activity?.findViewById(R.id.imageView)!!
                imageView.setImageBitmap(bitmap)
                baos.reset()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}