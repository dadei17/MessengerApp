package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import android.text.SpannableStringBuilder
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ge.dadeishvili.messengerapp.adapters.ChatAdapter
import ge.dadeishvili.messengerapp.fragments.MessageFragment.Companion.CHATS_DB
import ge.dadeishvili.messengerapp.models.Message
import ge.dadeishvili.messengerapp.models.Chat as ChatModel
import java.util.*
import kotlin.collections.ArrayList


class Chat() : Fragment() {
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
    private lateinit var sendButton: Button
    private lateinit var message: EditText
    private lateinit var chatsRef: DatabaseReference
    private lateinit var chatList: ChatModel


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
//            findNavController().navigate(R.id.action_back_to_search)
        }

        sendButton.setOnClickListener{
            val msg = message.text.toString()
            if (msg != "") {
                val from = nickName
                val to = chatUser
                val chatId = if (from < to) "$from-$to" else "$to-$from"

                val chat = ChatModel(listOf(Message(from, to, msg, Date())))

                chatList.messages = chatList.messages!!.toMutableList()
                (chatList.messages as MutableList<Message>).add(Message(from, to, msg, Date()))
                chatsRef.child(chatId).setValue(chatList)
            }
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
        storageRef = Firebase.storage.reference
        sendButton = view.findViewById(R.id.send_button)
        message = view.findViewById(R.id.chat_message_text)
        chatsRef = Firebase.database.getReference(CHATS_DB)
        chatList = ChatModel()

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)


        updateMessageList()
        recycler = view.findViewById(R.id.chat_recyclerview)
        recycler.layoutManager = LinearLayoutManager(context)
//        recycler.adapter = ChatAdapter(chatList)
    }

    private fun setInfo() {
        name.text = chatUser

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.child(chatUser).value as HashMap<*, *>
                val todoMap = hashMap["todo"] as String
                val text = SpannableStringBuilder(todoMap)
                todo.text = text.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        setImage(
            storageRef,
            chatUser,
            imageView,
            dialog
        )
    }

    private fun updateMessageList(){
        val from = nickName
        val to = chatUser
        val chatId = if (from < to) "$from-$to" else "$to-$from"

        chatsRef.child(chatId).get().addOnSuccessListener {
            val chatExisting = it.getValue(ChatModel::class.java)
            if (chatExisting != null) {
                chatList = ChatModel(chatExisting.messages!!.toMutableList())
            }
        }
    }

    private fun createChat(to: String) {
        var key = if (nickName < to) "$nickName-$to" else "$to-$nickName"
        usersRef.child(key)
    }
}