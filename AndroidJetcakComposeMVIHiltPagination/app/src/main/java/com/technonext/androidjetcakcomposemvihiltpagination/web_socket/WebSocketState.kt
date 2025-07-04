package com.technonext.androidjetcakcomposemvihiltpagination.web_socket

data class WebSocketState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val errorMessage: String? = null,
    val connectionStatus: String = "Disconnected"
)