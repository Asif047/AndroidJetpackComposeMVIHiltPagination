package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain

import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model.PrivateMessage
import kotlinx.coroutines.flow.StateFlow

interface PrivateMessageRepository {
    val connectionStatus: StateFlow<WebSocketConnectionStatus>
    val messages: StateFlow<List<PrivateMessage>>

    suspend fun connect(username: String)
    suspend fun disconnect()
    suspend fun sendPrivateMessage(receiverName: String, message: String)
    fun isConnected(): Boolean
    fun clearMessages()
}