package domain.shadowss.manager

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import defpackage.marsh.*
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.ByteArray

@Suppress("SpellCheckingInspection")
interface WebSocketCallback {

    fun onSAPI(instance: SAPI)

    fun onSAPO(instance: SAPO)

    fun onSARM(instance: SARM)

    fun onSARR(instance: SARR)

    fun onSARV(instance: SARV)

    fun onSCNG(instance: SCNG)

    fun onSMNG(instance: SMNG)
}

@Suppress("MemberVisibilityCanBePrivate")
class WebSocketManager : Manager {

    val observer = PublishSubject.create<Any>().toSerialized()

    private var webSocket: WebSocket? = null

    private val listener = object : WebSocketAdapter() {

        override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
            observer.onNext(frame?.payload ?: return)
        }
    }

    override fun init(vararg args: Any?) {
    }

    @Synchronized
    private fun connect(): Boolean {
        if (webSocket?.isOpen == true) {
            return true
        }
        return try {
            webSocket = WebSocketFactory()
                .setConnectionTimeout(1000)
                .createSocket("ws://8081.ru")
                .addListener(listener)
                .connect()
            true
        } catch (e: Throwable) {
            Timber.e(e)
            false
        }
    }

    @Synchronized
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

    @Synchronized
    fun send(instance: Any) {
        webSocket?.apply {
            if (isOpen) {
                sendBinary(marshal(instance))
            }
        }
    }

    @Synchronized
    fun disconnect() {
        webSocket?.disconnect()
    }

    private fun marshal(instance: Any): ByteArray {
        val output = ByteArrayOutputStream()
        try {
            val cls = instance.javaClass
            output.write(cls.simpleName.toByteArray())
            val marshal = cls.getMethod("marshal", OutputStream::class.java, ByteArray::class.java)
            marshal.invoke(instance, output, null)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return output.toByteArray()
    }

    private fun unmarshal(bytes: ByteArray): Any? {
        try {
            val name = String(bytes, 0, 4)
            val cls = Class.forName("defpackage.marsh.$name\$Unmarshaller")
            val constructor = cls.getConstructor(InputStream::class.java, ByteArray::class.java)
            val instance =
                constructor.newInstance(ByteArrayInputStream(bytes, 4, bytes.size - 4), null)
            val unmarshal = cls.getMethod("next")
            return unmarshal.invoke(instance)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return null
    }

    override fun release(vararg args: Any?) {}
}
