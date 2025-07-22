package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model

data class PrivateMessage(
    val id: String? = System.currentTimeMillis().toString(),
    val senderName: String,
    val receiverName: String,
    val message: String,
    val date: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOwnMessage: Boolean = false
)

sealed class PrivateMessageIntent {
    data class Connect(val username: String) : PrivateMessageIntent()
    object Disconnect : PrivateMessageIntent()
    data class SendMessage(val receiverName: String, val message: String) : PrivateMessageIntent()
    object ClearMessages : PrivateMessageIntent()
    object ClearError : PrivateMessageIntent()
    data class SetReceiver(val receiverName: String) : PrivateMessageIntent()
    data class SetUsername(val username: String) : PrivateMessageIntent()
}

data class PrivateMessageState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val messages: List<PrivateMessage> = emptyList(),
    val errorMessage: String? = null,
    val connectionStatus: String = "Disconnected",
    val username: String = "",
    val receiverName: String = ""
)