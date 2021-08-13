package ge.dadeishvili.messengerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.fragments.Profile.Companion.setImage
import ge.dadeishvili.messengerapp.fragments.SearchUser

class UsersAdapter(var userFragment: SearchUser) : RecyclerView.Adapter<UserItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemHolder {
        return UserItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserItemHolder, position: Int) {
        val user = userFragment.users[position]
        holder.name.text = user.nickName
        holder.todo.text = user.todo
        setImage(
            userFragment.storageRef,
            user.nickName!!,
            holder.image,
            userFragment.dialog,
        )
        holder.itemView.setOnClickListener {
            val bundle = bundleOf("nickName" to user.nickName)
            NavHostFragment.findNavController(userFragment).navigate(R.id.action_searchUsers_to_chat, bundle)
        }
    }

    override fun getItemCount(): Int {
        return userFragment.users.size
    }
}

class UserItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.findViewById(R.id.userLayout_nameText)
    var todo: TextView = itemView.findViewById(R.id.userLayout_toDo)
    var image: ImageView = itemView.findViewById(R.id.userLayout_image)
}