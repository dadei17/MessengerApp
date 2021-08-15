package ge.dadeishvili.messengerapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.adapters.MessageAdapter
import ge.dadeishvili.messengerapp.models.Chat
import java.util.*
import kotlin.properties.Delegates


class MessageFragment : Fragment() {
    private lateinit var plusButton: FloatingActionButton
    private lateinit var searchView: SearchView
    private lateinit var settingButton: Button
    private lateinit var recycler: RecyclerView
    private lateinit var chatRef: DatabaseReference
    lateinit var chats: MutableList<Chat>
    lateinit var chatsFiltered: MutableList<Chat>
    lateinit var nickName: String
    lateinit var storageRef: StorageReference
    lateinit var dialog: AlertDialog
    private var tryCount by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                chatsFiltered = chats.filter { curChat -> getChatName(curChat).contains(newText.toString()) } as MutableList<Chat>
                recycler.adapter!!.notifyDataSetChanged()
                return false
            }
        })

        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                showMyChat()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val intent = Intent()
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        }

        return view
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
        chatsFiltered = mutableListOf()
        storageRef = Firebase.storage.reference
        tryCount = 0
    }

    private fun getChatName(chat : Chat) : String{
        val messages = chat.messages
        val name = if (messages?.last()?.from?.compareTo(nickName) == 0)  messages.last().to else messages?.last()?.from
        return name.toString()
    }

    private fun calculateDiffInTime(date: Date?): Int {
        val c = Calendar.getInstance()
        return c.time.time.minus(date!!.time).toInt()
    }

    private fun showMyChat() {
        chatRef.get().addOnSuccessListener {
            chats.clear()
            for (child in it.children) {
                val twoNames = child.key
                val list = twoNames!!.split(SPLIT_CHAR)
                if (list.contains(nickName)) {
                    val chat = child.getValue(Chat::class.java)!!
                    chats.add(chat)
                }
            }
            chats.sortWith(Comparator { first, second ->
                calculateDiffInTime(first.messages?.get(first.messages!!.size - 1)?.date) -
                        calculateDiffInTime(second.messages?.get(second.messages!!.size - 1)?.date)
            })
            chatsFiltered = chats
            recycler.adapter!!.notifyDataSetChanged()
        }.addOnFailureListener {
            if (tryCount == 5) return@addOnFailureListener
            showMyChat()
            tryCount++
        }
    }

    companion object {
        const val SPLIT_CHAR = '-'
        const val CHATS_DB = "Chats"

        fun getNickName(): String {
            val email = Firebase.auth.currentUser!!.email
            return email!!.substring(0, email.indexOf('@'))
        }
    }
}