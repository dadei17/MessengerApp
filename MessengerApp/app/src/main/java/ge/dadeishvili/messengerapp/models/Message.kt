package ge.dadeishvili.messengerapp.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Message (
    var from: String? = null,
    var to: String? = null,
    var message: String? = null,
    var date: Date? = null
)