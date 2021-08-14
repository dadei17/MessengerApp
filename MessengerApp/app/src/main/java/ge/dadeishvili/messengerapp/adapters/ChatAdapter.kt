package ge.dadeishvili.messengerapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.fragments.Chat
import ge.dadeishvili.messengerapp.fragments.MessageFragment.Companion.getNickName
import ge.dadeishvili.messengerapp.models.Message
import ge.dadeishvili.messengerapp.models.Chat as ChatModel

class ChatAdapter(var chatList: ChatModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val LEFT_ITEM : Int = 0
    private val RIGHT_ITEM : Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == LEFT_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left_layout, parent, false)
            ChatItemHolderLeft(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right_layout, parent, false)
            ChatItemHolderRight(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == LEFT_ITEM) {
            (holder as ChatItemHolderLeft).bind(chatList.messages!![position])
        }else {
            (holder as ChatItemHolderRight).bind(chatList.messages!![position])
        }
    }

    override fun getItemCount(): Int {
        return chatList.messages!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatList.messages!![position].from == getNickName()){
            RIGHT_ITEM
        }else {
            LEFT_ITEM
        }
    }

    class ChatItemHolderRight(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageView: TextView = itemView.findViewById(R.id.chat_right_msg)

        fun bind(msg : Message){
            messageView.text = msg.toString()
        }
    }

    class ChatItemHolderLeft(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageView: TextView = itemView.findViewById(R.id.chat_left_msg)

        fun bind(msg : Message){
            messageView.text = msg.toString()

        }
    }

}

