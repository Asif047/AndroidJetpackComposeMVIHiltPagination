package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.presentation

// WebSocketViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.ChatMessage
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketIntent
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketState
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.domain.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebSocketViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WebSocketState())
    val state: StateFlow<WebSocketState> = _state.asStateFlow()

    init {
        // Observe connection status
        viewModelScope.launch {
            webSocketRepository.connectionStatus.collect { status ->
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

                if (status == WebSocketConnectionStatus.CONNECTED) {
                    subscribeToMessages()
                }
            }
        }
    }

    private fun subscribeToMessages() {
        viewModelScope.launch {
            webSocketRepository.subscribeToTopic("/topic/messages").collect { message ->
                if (message.isNotEmpty()) {
                    val newMessage = ChatMessage(
                        message = message,
                        isOwnMessage = false
                    )

                    _state.value = _state.value.copy(
                        messages = _state.value.messages + newMessage
                    )
                }
            }
        }
    }

    fun handleIntent(intent: WebSocketIntent) {
        when (intent) {
            is WebSocketIntent.Connect -> {
                viewModelScope.launch {
                    try {
                        webSocketRepository.connect()
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            errorMessage = "Failed to connect: ${e.message}"
                        )
                    }
                }
            }

            is WebSocketIntent.Disconnect -> {
                viewModelScope.launch {
                    try {
                        webSocketRepository.disconnect()
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            errorMessage = "Failed to disconnect: ${e.message}"
                        )
                    }
                }
            }

            is WebSocketIntent.SendMessage -> {
                if (intent.message.isNotBlank()) {
                    viewModelScope.launch {
                        try {
                            // Add the message to our local state first (as sent message)
                            val ownMessage = ChatMessage(
                                message = intent.message,
                                isOwnMessage = true
                            )

                            _state.value = _state.value.copy(
                                messages = _state.value.messages + ownMessage
                            )

                            // Send the message via WebSocket
                            webSocketRepository.sendMessage("/app/send", intent.message)
                        } catch (e: Exception) {
                            _state.value = _state.value.copy(
                                errorMessage = "Failed to send message: ${e.message}"
                            )
                        }
                    }
                }
            }

            is WebSocketIntent.ClearMessages -> {
                _state.value = _state.value.copy(messages = emptyList())
            }

            is WebSocketIntent.ClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            webSocketRepository.disconnect()
        }
    }
}