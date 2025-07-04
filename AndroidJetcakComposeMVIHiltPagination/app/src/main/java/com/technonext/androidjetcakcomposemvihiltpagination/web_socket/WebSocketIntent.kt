package com.technonext.androidjetcakcomposemvihiltpagination.web_socket

sealed class WebSocketIntent {
    object Connect : WebSocketIntent()
    object Disconnect : WebSocketIntent()
    data class SendMessage(val message: String) : WebSocketIntent()
    object ClearMessages : WebSocketIntent()
    object ClearError : WebSocketIntent()
}