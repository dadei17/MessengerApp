package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User

class Message : Fragment() {
    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.messages, container, false)
        var searchView = view.findViewById(R.id.messages_search) as SearchView

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
}