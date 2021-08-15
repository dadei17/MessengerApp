package ge.dadeishvili.messengerapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.adapters.UsersAdapter
import ge.dadeishvili.messengerapp.models.User
import kotlinx.coroutines.*


class SearchUser : Fragment() {
    private lateinit var backButton: ImageButton
    private lateinit var usersRef: DatabaseReference
    private lateinit var recycler: RecyclerView
    private lateinit var nickName: String
    private lateinit var search: SearchView
    lateinit var users: MutableList<User>
    lateinit var storageRef: StorageReference
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = layoutInflater.inflate(R.layout.loading, null)
        dialog = builder.setView(customLayout).create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_users, container, false)
        init(view)
        showUsers()
        search()
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchUser_to_message)
        }
        return view
    }

    private fun init(view: View) {
        backButton = view.findViewById(R.id.search_page_back)
        usersRef = Firebase.database.getReference(SignUp.USERS_DB)
        recycler = view.findViewById(R.id.search_page_recyclerview)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = UsersAdapter(this)
        nickName = MessageFragment.getNickName()
        users = mutableListOf()
        storageRef = Firebase.storage.reference
        search = view.findViewById(R.id.search_page_search)
    }

    private fun showUsers(searchVal : String = "", performedSearch : Boolean = false) {
        usersRef.get().addOnSuccessListener {
            users.clear()
            for (child in it.children) {
                val user = child.getValue(User::class.java)!!
                if (user.nickName != nickName && user.nickName?.contains(searchVal) == true) {
                    users.add(user)
                }
            }
            recycler.adapter!!.notifyDataSetChanged()
            if(performedSearch && users.size == 0) {
                Log.e("list", "empty")
                Toast.makeText(context, "Entered user doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
    var queryTextChangedJob: Job? = null
    private fun search() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("submit", query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText ?: return false
                var searchText = text
                if (newText?.length!! >= 3) {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(500)
                        showUsers(searchText, true)
                    }
                }else {
                    showUsers()
                }
                return true
            }
        })
    }
}