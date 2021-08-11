package ge.dadeishvili.messengerapp.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.fragments.MessageFragment
import ge.dadeishvili.messengerapp.fragments.Profile
import java.util.*


class MessageAdapter(var messageFragment: MessageFragment) :
    RecyclerView.Adapter<MessageItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemHolder {
        return MessageItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageItemHolder, position: Int) {
        val chat = messageFragment.chats[position]
        val nickname = messageFragment.nickName
        val messages = chat.messages
        holder.message.text = messages?.last()?.message
        holder.name.text = if (messages?.last()?.from?.compareTo(nickname) == 0)  messages.last().to else messages?.last()?.from
        holder.time.text = calculateTime(messages?.last()?.date)
        holder.itemView.setOnClickListener {
            NavHostFragment.findNavController(messageFragment).navigate(R.id.action_message_to_chat)
        }
        Profile.setImage(
            messageFragment.storageRef,
            nickname,
            holder.image,
            messageFragment.dialog,
            messageFragment.requireContext()
        )
    }

    override fun getItemCount(): Int {
        return messageFragment.chats.size
    }

    private fun calculateTime(date: Date?): String {
        val monthName = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        val c = Calendar.getInstance()
        val diff: Long = c.time.time.minus(date!!.time)
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hour = minutes / 60
        if (hour > 24) {
            c.time = date
            return c.get(Calendar.DAY_OF_MONTH).toString() + " " + monthName[c.get(Calendar.MONTH)]
        }
        if (hour > 0) return "$hour hour"
        return "$minutes min"
    }
}


class MessageItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.findViewById(R.id.messageLayout_nameText)
    var message: TextView = itemView.findViewById(R.id.messageLayout_message)
    var time: TextView = itemView.findViewById(R.id.messageLayout_time)
    var image: ImageView = itemView.findViewById(R.id.messageLayout_image)
}