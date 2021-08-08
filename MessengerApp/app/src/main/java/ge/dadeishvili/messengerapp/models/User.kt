package ge.dadeishvili.messengerapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var nickName: String? = null,
    var todo: String? = null,
    var avatar: ByteArray? = null
)

