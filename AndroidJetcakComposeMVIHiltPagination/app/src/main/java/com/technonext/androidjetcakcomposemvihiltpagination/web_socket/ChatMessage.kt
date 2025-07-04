package com.technonext.androidjetcakcomposemvihiltpagination.web_socket

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOwnMessage: Boolean = false
)