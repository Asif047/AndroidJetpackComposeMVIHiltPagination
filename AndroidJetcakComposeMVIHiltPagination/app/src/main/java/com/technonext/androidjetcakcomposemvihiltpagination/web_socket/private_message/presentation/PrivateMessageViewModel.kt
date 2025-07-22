package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.PrivateMessageRepository
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model.PrivateMessageIntent
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model.PrivateMessageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateMessageViewModel @Inject constructor(
    private val privateMessageRepository: PrivateMessageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PrivateMessageState())
    val state: StateFlow<PrivateMessageState> = _state.asStateFlow()

    init {
        // Observe connection status
        viewModelScope.launch {
            privateMessageRepository.connectionStatus
                .collect { status ->
                    Log.d("ViewModel", "Connection status changed to: $status")
                    _state.value = _state.value.copy(
                        isConnected = status == WebSocketConnectionStatus.CONNECTED,
                        isConnecting = status == WebSocketConnectionStatus.CONNECTING,
                        connectionStatus = when (status) {
                            WebSocketConnectionStatus.CONNECTING -> "Connecting..."
                            WebSocketConnectionStatus.CONNECTED -> "Connected"
                            WebSocketConnectionStatus.DISCONNECTED -> "Disconnected"
                            WebSocketConnectionStatus.ERROR -> "Connection Error"
                        },
                        errorMessage = if (status == WebSocketConnectionStatus.ERROR) {
                            "Failed to connect to WebSocket"
                        } else null
                    )
                }
        }

        // Observe messages with explicit logging
        viewModelScope.launch {
            privateMessageRepository.messages
                .collect { messages ->
                    Log.d("ViewModel", "Messages updated, count: ${messages.size}")
                    if (messages.isNotEmpty()) {
                        Log.d("ViewModel", "Latest message: ${messages.last().message}")
                    }
                    _state.value = _state.value.copy(messages = messages)
                }
        }
    }

    fun handleIntent(intent: PrivateMessageIntent) {
        when (intent) {
            is PrivateMessageIntent.Connect -> {
                viewModelScope.launch {
                    try {
                        if (intent.username.isNotBlank()) {
                            Log.d("ViewModel", "Connecting with username: ${intent.username}")
                            _state.value = _state.value.copy(username = intent.username)
                            privateMessageRepository.connect(intent.username)
                        }
                    } catch (e: Exception) {
                        Log.e("ViewModel", "Connection failed", e)
                        _state.value = _state.value.copy(
                            errorMessage = "Failed to connect: ${e.message}"
                        )
                    }
                }
            }

            is PrivateMessageIntent.Disconnect -> {
                viewModelScope.launch {
                    try {
                        Log.d("ViewModel", "Disconnecting...")
                        privateMessageRepository.disconnect()
                    } catch (e: Exception) {
                        Log.e("ViewModel", "Disconnect failed", e)
                        _state.value = _state.value.copy(
                            errorMessage = "Failed to disconnect: ${e.message}"
                        )
                    }
                }
            }

            is PrivateMessageIntent.SendMessage -> {
                if (intent.message.isNotBlank() && intent.receiverName.isNotBlank()) {
                    viewModelScope.launch {
                        try {
                            Log.d("ViewModel", "Sending message to ${intent.receiverName}: ${intent.message}")
                            privateMessageRepository.sendPrivateMessage(intent.receiverName, intent.message)
                        } catch (e: Exception) {
                            Log.e("ViewModel", "Send message failed", e)
                            _state.value = _state.value.copy(
                                errorMessage = "Failed to send message: ${e.message}"
                            )
                        }
                    }
                }
            }

            is PrivateMessageIntent.ClearMessages -> {
                Log.d("ViewModel", "Clearing messages")
                privateMessageRepository.clearMessages()
            }

            is PrivateMessageIntent.ClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }

            is PrivateMessageIntent.SetReceiver -> {
                _state.value = _state.value.copy(receiverName = intent.receiverName)
            }

            is PrivateMessageIntent.SetUsername -> {
                _state.value = _state.value.copy(username = intent.username)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            try {
                privateMessageRepository.disconnect()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error during cleanup", e)
            }
        }
    }
}