package ge.dadeishvili.messengerapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.dadeishvili.messengerapp.fragments.Chat
import ge.dadeishvili.messengerapp.models.Message
import ge.dadeishvili.messengerapp.models.Chat as ChatModel

class ChatAdapter(var chatList: ChatModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}

class ChatItemHolderRight(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(postModel : Message){

    }
}

class ChatItemHolderLeft(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(postModel : Message){

    }
}