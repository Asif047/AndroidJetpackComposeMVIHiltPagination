package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.data

import com.asif047.androidjetpackcomposemvihiltpagination.BuildConfig
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.domain.WebSocketRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor() : WebSocketRepository {

    private var stompClient: StompClient? = null
    private val _connectionStatus = MutableStateFlow(WebSocketConnectionStatus.DISCONNECTED)
    private val _messages = MutableSharedFlow<String>()

    override val connectionStatus: StateFlow<WebSocketConnectionStatus> = _connectionStatus
    override val messages: Flow<String> = _messages

    override suspend fun connect() {
        if (stompClient != null && stompClient!!.isConnected) {
            return
        }

        _connectionStatus.value = WebSocketConnectionStatus.CONNECTING

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.WEBSOCKET_URL)

        stompClient?.connect()

        stompClient?.lifecycle()
            ?.subscribe {
                when (it.type) {
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED -> {
                        _connectionStatus.value = WebSocketConnectionStatus.CONNECTED
                    }
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR -> {
                        _connectionStatus.value = WebSocketConnectionStatus.ERROR
                    }
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED -> {
                        _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                    }
                    else -> {}
                }
            }
    }

    override suspend fun disconnect() {
        stompClient?.disconnect()
        stompClient = null
        _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
    }

    override suspend fun sendMessage(destination: String, message: String) {
        stompClient?.send(destination, message)?.subscribe()
    }

    override fun subscribeToTopic(topic: String): Flow<String> {
        return callbackFlow {
            val topicSubscription = stompClient?.topic(topic)?.subscribe {
                trySend(it.payload)
            }
            awaitClose {
                topicSubscription?.dispose()
            }
        }
    }

    override fun isConnected(): Boolean {
        return _connectionStatus.value == WebSocketConnectionStatus.CONNECTED
    }
}