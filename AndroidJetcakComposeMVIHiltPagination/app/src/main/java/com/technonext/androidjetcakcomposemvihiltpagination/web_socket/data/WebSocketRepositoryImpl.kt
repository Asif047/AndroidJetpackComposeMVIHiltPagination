package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.data

import com.asif047.androidjetpackcomposemvihiltpagination.BuildConfig
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.domain.WebSocketRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketRepository {

    private var webSocket: WebSocket? = null
    private val _connectionStatus = MutableStateFlow(WebSocketConnectionStatus.DISCONNECTED)
    private val _messages = MutableStateFlow("")

    override val connectionStatus: StateFlow<WebSocketConnectionStatus> = _connectionStatus
    override val messages: Flow<String> = callbackFlow {
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                _connectionStatus.value = WebSocketConnectionStatus.CONNECTED
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                trySend(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                trySend(bytes.utf8())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                _connectionStatus.value = WebSocketConnectionStatus.ERROR
            }
        }

        this@WebSocketRepositoryImpl.webSocket?.let {
            // If we already have a WebSocket, add the listener
            // Note: This is a simplified approach. In a real app, you might want to handle this differently
        }

        awaitClose {
            // Cleanup if needed
        }
    }

    override suspend fun connect() {
        if (webSocket != null) {
            disconnect()
        }

        _connectionStatus.value = WebSocketConnectionStatus.CONNECTING

        val request = Request.Builder()
            .url(BuildConfig.WEBSOCKET_URL)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                _connectionStatus.value = WebSocketConnectionStatus.CONNECTED
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                _messages.value = text
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                _messages.value = bytes.utf8()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                _connectionStatus.value = WebSocketConnectionStatus.ERROR
            }
        }

        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    override suspend fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
    }

    override suspend fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    override fun isConnected(): Boolean {
        return _connectionStatus.value == WebSocketConnectionStatus.CONNECTED
    }
}