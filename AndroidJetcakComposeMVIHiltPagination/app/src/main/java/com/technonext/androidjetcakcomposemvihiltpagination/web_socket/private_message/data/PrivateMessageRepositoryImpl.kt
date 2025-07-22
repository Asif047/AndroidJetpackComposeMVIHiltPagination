package com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.data

import android.util.Log
import com.asif047.androidjetpackcomposemvihiltpagination.BuildConfig
import com.google.gson.Gson
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.WebSocketConnectionStatus
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.PrivateMessageRepository
import com.technonext.androidjetcakcomposemvihiltpagination.web_socket.private_message.domain.model.PrivateMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivateMessageRepositoryImpl @Inject constructor() : PrivateMessageRepository {

    private var stompClient: StompClient? = null
    private val _connectionStatus = MutableStateFlow(WebSocketConnectionStatus.DISCONNECTED)
    private val _messages = MutableStateFlow<List<PrivateMessage>>(emptyList())
    private var currentUsername: String = ""
    private var subscriptionDisposable: Disposable? = null
    private var lifecycleDisposable: Disposable? = null
    private val gson = Gson()

    override val connectionStatus: StateFlow<WebSocketConnectionStatus> = _connectionStatus
    override val messages: StateFlow<List<PrivateMessage>> = _messages

    override suspend fun connect(username: String) {
        withContext(Dispatchers.IO) {
            if (stompClient != null && stompClient!!.isConnected) {
                disconnect()
            }

            currentUsername = username
            _connectionStatus.value = WebSocketConnectionStatus.CONNECTING

            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.WEBSOCKET_URL)

            // Set up lifecycle listener
            lifecycleDisposable = stompClient?.lifecycle()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED -> {
                            _connectionStatus.value = WebSocketConnectionStatus.CONNECTED
                            // Subscribe to private messages once connected
                            subscribeToPrivateMessages()
                        }
                        ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR -> {
                            _connectionStatus.value = WebSocketConnectionStatus.ERROR
                            Log.e("WebSocket", "Connection error: ${lifecycleEvent.exception?.message}")
                        }
                        ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED -> {
                            _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
                        }
                        else -> {}
                    }
                }

            stompClient?.connect()
        }
    }

    override suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            // Dispose of subscription
            subscriptionDisposable?.dispose()
            subscriptionDisposable = null

            // Dispose of lifecycle listener
            lifecycleDisposable?.dispose()
            lifecycleDisposable = null

            // Disconnect STOMP client
            stompClient?.disconnect()
            stompClient = null
            _connectionStatus.value = WebSocketConnectionStatus.DISCONNECTED
        }
    }

    override suspend fun sendPrivateMessage(receiverName: String, message: String) {
        withContext(Dispatchers.IO) {
            stompClient?.let { client ->
                if (client.isConnected) {
                    val messagePayload = PrivateMessage(
                        senderName = currentUsername,
                        receiverName = receiverName,
                        message = message,
                        date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
                    )

                    val messageJson = gson.toJson(messagePayload)

                    client.send("/app/private-message", messageJson)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                // Message sent successfully
                                Log.d("WebSocket", "Private message sent successfully")

                                // Add to local messages as sent - ensure this runs on main thread
                                addMessageToList(messagePayload.copy(isOwnMessage = true))
                            },
                            { error ->
                                Log.e("WebSocket", "Failed to send private message: ${error.message}")
                            }
                        )
                } else {
                    Log.e("WebSocket", "Cannot send message: STOMP client not connected")
                }
            }
        }
    }

    override fun isConnected(): Boolean {
        return stompClient?.isConnected == true &&
                _connectionStatus.value == WebSocketConnectionStatus.CONNECTED
    }

    override fun clearMessages() {
        _messages.value = emptyList()
    }

    private fun subscribeToPrivateMessages() {
        stompClient?.let { client ->
            val destination = "/user/$currentUsername/private"
            Log.d("WebSocket", "Subscribing to: $destination")

            subscriptionDisposable = client.topic(destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // Important: observe on main thread
                .subscribe(
                    { stompMessage ->
                        Log.d("WebSocket", "Received private message: ${stompMessage.payload}")
                        handleReceivedMessage(stompMessage.payload)
                    },
                    { error ->
                        Log.e("WebSocket", "Error receiving private message: ${error.message}")
                    }
                )
        }
    }

    private fun handleReceivedMessage(messagePayload: String) {
        try {
            val privateMessage = gson.fromJson(messagePayload, PrivateMessage::class.java)

            // Add received message to list - this should run on main thread due to observeOn above
            addMessageToList(privateMessage.copy(isOwnMessage = false))

            Log.d("WebSocket", "Private message added to list: ${privateMessage.message}")
        } catch (e: Exception) {
            Log.e("WebSocket", "Failed to parse private message: ${e.message}")
        }
    }

    // Helper function to safely add messages to the list
    private fun addMessageToList(message: PrivateMessage) {
        val currentMessages = _messages.value.toMutableList()
        currentMessages.add(message)
        _messages.value = currentMessages

        Log.d("WebSocket", "Total messages now: ${_messages.value.size}")
    }
}