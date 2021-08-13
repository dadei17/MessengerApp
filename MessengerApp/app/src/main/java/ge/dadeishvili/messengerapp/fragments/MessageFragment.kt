package ge.dadeishvili.messengerapp.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.adapters.MessageAdapter
import ge.dadeishvili.messengerapp.models.Chat

class MessageFragment : Fragment() {
    private lateinit var plusButton: FloatingActionButton
    private lateinit var searchView: SearchView
    private lateinit var settingButton: Button
    private lateinit var recycler: RecyclerView
    private lateinit var chatRef: DatabaseReference
    lateinit var chats: MutableList<Chat>
    lateinit var nickName: String
    lateinit var storageRef: StorageReference
    lateinit var dialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.messages, container, false)
        init(view)
        showMyChat()
        plusButton.setOnClickListener {
            findNavController().navigate(R.id.action_message_to_searchUser)
        }
        settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_message_to_profile)
        }

//        searchView.onActionViewExpanded()
//        if (!searchView.isFocused()) {
//            searchView.clearFocus()
//        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return view
    }

    private fun add(){
//        val chat = Chat(listOf(Message("dito", "pertaxa", "ravaxar?", Date())))
//        Firebase.database.getReference(CHATS_DB).child("dito-pertaxa").setValue(chat)
//        Firebase.database.getReference(CHATS_DB).child("dito-pertaxa").get().addOnSuccessListener {
//            val chat = it.getValue(Chat::class.java)
//            if (chat != null) {
//                val list = chat.messages!!.toMutableList()
//                list.add(Message("pertaxa", "dito", "shen?", Date()))
//                Firebase.database.getReference(CHATS_DB).child("dito-pertaxa").setValue(Chat(list))
//            }
//        }
    }

    private fun init(view: View) {
        plusButton = view.findViewById(R.id.messages_fab_button)
        settingButton = view.findViewById(R.id.messages_setting_button)
        searchView = view.findViewById(R.id.messages_search)
        recycler = view.findViewById(R.id.messages_recyclerview)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = MessageAdapter(this)
        chatRef = Firebase.database.getReference(CHATS_DB)
        nickName = getNickName()
        chats = mutableListOf()
        storageRef = Firebase.storage.reference
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()
    }

    private fun showMyChat() {
        chatRef.get().addOnSuccessListener {
            for (child in it.children) {
//                val twoNames = child.key
//                val list = twoNames!!.split(SPLIT_CHAR)
//                if (list.contains(nickName)) {
//                    val chat = child.getValue(Chat::class.java)!!
//                    chats.add(chat)
//                    recycler.adapter!!.notifyDataSetChanged()
//                }
            }
        }
    }

    companion object {
        const val SPLIT_CHAR = '-'
        const val CHATS_DB = "Chats"

        fun getNickName() : String{
            val email = Firebase.auth.currentUser!!.email
            return email!!.substring(0, email.indexOf('@'))
        }
    }
}