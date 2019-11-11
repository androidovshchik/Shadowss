package domain.shadowss.manager

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import domain.shadowss.extension.toHex
import timber.log.Timber
import java.io.IOException

@Suppress("MemberVisibilityCanBePrivate")
class WebSocketManager : Manager {

    private var webSocket: WebSocket? = null

    private val listener = object : WebSocketAdapter() {

        override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray) {
            Timber.e(binary.toHex())
        }

        override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {
            Timber.e(cause)
        }

        override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
            Timber.e(cause)
        }

        override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {
            Timber.e(cause)
        }
    }

    override fun init(vararg args: Any?) {
    }

    @Throws(IOException::class, WebSocketException::class)
    private fun connect() {
        if (webSocket?.isOpen == true) {
            return
        }
        try {
            webSocket = WebSocketFactory()
                .setConnectionTimeout(5000)
                .createSocket("ws://8081.ru")
                .addListener(listener)
                .connect()
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    fun reconnect() {
        webSocket?.recreate()
            ?.connect()
    }

    fun disconnect() {
        webSocket?.disconnect()
    }

    override fun release(vararg args: Any?) {}
}
