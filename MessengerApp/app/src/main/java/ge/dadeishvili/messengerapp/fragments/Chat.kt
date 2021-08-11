package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
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
//import ge.dadeishvili.messengerapp.adapters.ChatAdapter

class Chat : Fragment() {
    private lateinit var name: TextView
    private lateinit var todo: TextView
    private lateinit var imageView: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var recycler: RecyclerView
    private lateinit var nickName: String
    private lateinit var usersRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat, container, false)
        init(view)
        setInfo()
        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_chat_to_message)
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
//        recycler.adapter = ChatAdapter(this)
    }

    private fun setInfo() {

    }

    private fun createChat(to: String) {
        var key = if (nickName < to) "$nickName-$to" else "$to-$nickName"
        usersRef.child(key)
    }
}