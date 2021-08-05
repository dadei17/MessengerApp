package ge.dadeishvili.messengerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ge.dadeishvili.messengerapp.R
import ge.dadeishvili.messengerapp.models.User

class Message : Fragment() {
    lateinit var user: User

    // TODO: user not serialized
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            user = requireArguments()["user"] as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.messages, container, false)
        return view
    }
}