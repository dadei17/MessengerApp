package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.fragments.Profile.Companion.setImage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import android.app.AlertDialog


//import ge.dadeishvili.messengerapp.adapters.ChatAdapter

class Chat : Fragment() {
    private lateinit var name: TextView
    private lateinit var todo: TextView
    private lateinit var imageView: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var recycler: RecyclerView
    private lateinit var nickName: String
    private lateinit var usersRef: DatabaseReference
    private lateinit var chatUser: String
    private lateinit var chatToDo: String
    private lateinit var storageRef: StorageReference
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat, container, false)
        init(view)
        setInfo()
        backButton.setOnClickListener{
            Log.e("clicked", "back")
            findNavController().navigate(R.id.action_back_to_search)
        }
        return view;
    }

    private fun init(view: View) {
        name = view.findViewById(R.id.chat_name)
        todo = view.findViewById(R.id.chat_to_do)
        imageView = view.findViewById(R.id.chat_image)
        backButton = view.findViewById(R.id.chat_back)
        nickName = MessageFragment.getNickName()
        usersRef = Firebase.database.getReference(SignUp.USERS_DB)
        recycler = view.findViewById(R.id.chat_recyclerview)
        recycler.layoutManager = LinearLayoutManager(context)
        chatUser = arguments?.getString("nickName").toString()
        chatToDo = arguments?.getString("toDo").toString()
        storageRef = Firebase.storage.reference

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
//        recycler.adapter = ChatAdapter(this)
    }

    private fun setInfo() {
        name.text = chatUser
        todo.text = chatToDo
        setImage(
            storageRef,
            chatUser,
            imageView,
            dialog
        )
    }

    private fun createChat(to: String) {
        var key = if (nickName < to) "$nickName-$to" else "$to-$nickName"
        usersRef.child(key)
    }
}