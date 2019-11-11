package domain.shadowss.manager

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import domain.shadowss.extension.toHex
import timber.log.Timber

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

    /**
     * Should be called from single background thread
     */
    private fun connect(): Boolean {
        if (webSocket?.isOpen == true) {
            return true
        }
        return try {
            webSocket = WebSocketFactory()
                .setConnectionTimeout(2000)
                .createSocket("ws://8081.ru")
                .addListener(listener)
                .connect()
            true
        } catch (e: Throwable) {
            Timber.e(e)
            false
        }
    }

    /**
     * Should be called from single background thread
     */
    fun reconnect(): Boolean {
        if (webSocket?.isOpen != false) {
            return connect()
        }
        return try {
            webSocket?.recreate()
                ?.connect()
            true
        } catch (e: Throwable) {
            Timber.e(e)
            false
        }
    }

    /**
     * Should be called from single background thread
     */
    fun disconnect() {
        webSocket?.disconnect()
    }

    override fun release(vararg args: Any?) {}
}
