package ge.dadeishvili.messengerapp.models

import java.io.Serializable

data class User (
    var id: Int = 0,
    var nick: String,
    var password: String,
    var todo: String,
    var avatar: ByteArray
): Serializable

