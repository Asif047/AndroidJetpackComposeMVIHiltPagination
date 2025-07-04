package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.domain

import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    val connectionStatus: Flow<WebSocketConnectionStatus>
    val messages: Flow<String>

    suspend fun connect()
    suspend fun disconnect()
    suspend fun sendMessage(message: String)
    fun isConnected(): Boolean
}