package ge.dadeishvili.messengerapp.models

data class Chat (
    var messages: List<Message>? = listOf()
)