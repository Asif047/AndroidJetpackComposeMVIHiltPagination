package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.data

import com.asif047.androidjetpackcomposemvihiltpagination.BuildConfig
import com.google.gson.Gson
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.PrivateMessageRepository
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model.PrivateMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivateMessageRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : PrivateMessageRepository {

    private var webSocket: WebSocket? = null
    private val _connectionStatus = MutableStateFlow(WebSocketConnectionStatus.DISCONNECTED)
    private val _messages = MutableStateFlow<List<PrivateMessage>>(emptyList())
    private var currentUsername: String = ""
    private var isSubscribed = false

    override val connectionStatus: StateFlow<WebSocketConnectionStatus> = _connectionStatus
    override val messages: StateFlow<List<PrivateMessage>> = _messages

    override suspend fun connect(username: String) {
        if (webSocket != null) {
            disconnect()
        }

        currentUsername = username
        _connectionStatus.value = WebSocketConnectionStatus.CONNECTING

        val request = Request.Builder()
            .url(BuildConfig.WEBSOCKET_URL)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Send STOMP CONNECT frame
                val connectFrame = buildStompConnectFrame()
                webSocket.send(connectFrame)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                handleStompMessage(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                handleStompMessage(bytes.utf8())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                isSubscribed = false
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                _connectionStatus.value = WebSocketConnectionStatus.ERROR
                isSubscribed = false
            }
        }

        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    override suspend fun disconnect() {
        if (isSubscribed) {
            unsubscribeFromPrivateMessages()
        }
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
        isSubscribed = false
    }

    override suspend fun sendPrivateMessage(receiverName: String, message: String) {
        webSocket?.let { ws ->
            val messagePayload = PrivateMessage(
                senderName = currentUsername,
                receiverName = receiverName,
                message = message,
                date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )

            val sendFrame = buildStompSendFrame(messagePayload)
            ws.send(sendFrame)

            // Add to local messages as sent
            val currentMessages = _messages.value.toMutableList()
            currentMessages.add(messagePayload.copy(isOwnMessage = true))
            _messages.value = currentMessages
        }
    }

    override fun isConnected(): Boolean {
        return _connectionStatus.value == WebSocketConnectionStatus.CONNECTED
    }

    override fun clearMessages() {
        _messages.value = emptyList()
    }

    private fun buildStompConnectFrame(): String {
        return """
            CONNECT
            accept-version:1.0,1.1,2.0
            heart-beat:10000,10000
            
            ${'\u0000'}
        """.trimIndent()
    }

    private fun buildStompSubscribeFrame(): String {
        return """
            SUBSCRIBE
            id:sub-0
            destination:/user/$currentUsername/private
            
            ${'\u0000'}
        """.trimIndent()
    }

    private fun buildStompSendFrame(message: PrivateMessage): String {
        val gson = Gson()
        val messageJson = gson.toJson(message)

        return """
            SEND
            destination:/app/private-message
            content-type:application/json
            
            $messageJson${'\u0000'}
        """.trimIndent()
    }

    private fun buildStompUnsubscribeFrame(): String {
        return """
            UNSUBSCRIBE
            id:sub-0
            
            ${'\u0000'}
        """.trimIndent()
    }

    private fun handleStompMessage(message: String) {
        val lines = message.split("\n")
        val command = lines.firstOrNull() ?: return

        when (command) {
            "CONNECTED" -> {
                _connectionStatus.value = WebSocketConnectionStatus.CONNECTED
                // Subscribe to private messages
                subscribeToPrivateMessages()
            }
            "MESSAGE" -> {
                // Parse message content
                val bodyIndex = lines.indexOf("") + 1
                if (bodyIndex < lines.size) {
                    val messageBody = lines.subList(bodyIndex, lines.size)
                        .joinToString("\n")
                        .replace("\u0000", "")

                    try {
                        val gson = Gson()
                        val privateMessage = gson.fromJson(messageBody, PrivateMessage::class.java)

                        // Add received message to list
                        val currentMessages = _messages.value.toMutableList()
                        currentMessages.add(privateMessage.copy(isOwnMessage = false))
                        _messages.value = currentMessages
                    } catch (e: Exception) {
                        // Handle JSON parsing error
                    }
                }
            }
            "ERROR" -> {
                _connectionStatus.value = WebSocketConnectionStatus.ERROR
            }
        }
    }

    private fun subscribeToPrivateMessages() {
        webSocket?.let { ws ->
            val subscribeFrame = buildStompSubscribeFrame()
            ws.send(subscribeFrame)
            isSubscribed = true
        }
    }

    private fun unsubscribeFromPrivateMessages() {
        webSocket?.let { ws ->
            val unsubscribeFrame = buildStompUnsubscribeFrame()
            ws.send(unsubscribeFrame)
            isSubscribed = false
        }
    }
}