package domain.shadowss.manager

import android.content.Context
import android.os.Build
import com.neovisionaries.ws.client.*
import defpackage.marsh.*
import domain.shadowss.BuildConfig
import domain.shadowss.extension.getTopActivity
import domain.shadowss.screen.*
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.activityManager
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

@Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
class WebSocketManager(context: Context) {

    val observer = PublishSubject.create<Any>().toSerialized()

    private val activityManager = context.activityManager

    private val packageName = context.packageName

    private var webSocket: WebSocket? = null

    private val listener = object : WebSocketAdapter() {

        override fun onConnected(
            websocket: WebSocket?,
            headers: MutableMap<String, MutableList<String>>?
        ) {
            Timber.d("onConnected")
        }

        override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
            frame?.payload?.let {
                val instance = unmarshal(it)
                if (instance != null) {
                    observer.onNext(instance)
                }
            }
        }

        override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
            Timber.e("onConnectError")
        }

        override fun onDisconnected(
            websocket: WebSocket?,
            serverCloseFrame: WebSocketFrame?,
            clientCloseFrame: WebSocketFrame?,
            closedByServer: Boolean
        ) {
            Timber.d("onDisconnected")
        }
    }

    @Synchronized
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
                when (instance) {
                    is ASER -> {
                        instance.apply {
                            os = "A"
                            api = Build.VERSION.RELEASE
                            model = "${Build.MANUFACTURER} ${Build.MODEL}"
                            form = currentForm
                            ver = "A${"%02d".format(BuildConfig.VERSION_CODE)}"
                            uid = ""
                            IP = ""
                            usid = ""
                        }
                    }
                    is ASRC -> {
                        instance.apply {
                            os = "A"
                            api = Build.VERSION.RELEASE
                            model = "${Build.MANUFACTURER} ${Build.MODEL}"
                            form = currentForm
                        }
                    }
                    is ASRM -> {
                        instance.apply {
                            os = "A"
                            api = Build.VERSION.RELEASE
                            model = "${Build.MANUFACTURER} ${Build.MODEL}"
                            form = currentForm
                        }
                    }
                    is ASRV -> {
                        instance.apply {
                            os = "A"
                            api = Build.VERSION.RELEASE
                            model = "${Build.MANUFACTURER} ${Build.MODEL}"
                            form = currentForm
                            ver = "A${"%02d".format(BuildConfig.VERSION_CODE)}"
                        }
                    }
                    is ASRR -> {
                        instance.apply {
                            os = "A"
                            api = Build.VERSION.RELEASE
                            model = "${Build.MANUFACTURER} ${Build.MODEL}"
                            form = currentForm
                        }
                    }
                }
                sendBinary(marshal(instance))
            }
        }
    }

    @Synchronized
    fun disconnect() {
        webSocket?.apply {
            if (isOpen) {
                webSocket?.disconnect()
            }
        }
    }

    private val currentForm: String
        get() = when (activityManager.getTopActivity(packageName)) {
            StartActivity::class.java.name, TermsActivity::class.java.name -> "REG1"
            RegistrationActivity::class.java.name -> "REG2"
            ManagerActivity::class.java.name -> "MNMA"
            DriverActivity::class.java.name -> "MNDR"
            else -> "NULL"
        }

    // todo optimization
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
        var name = "NULL"
        try {
            name = String(bytes, 0, 4)
            val cls = Class.forName("defpackage.marsh.$name\$Unmarshaller")
            val constructor = cls.getConstructor(InputStream::class.java, ByteArray::class.java)
            val instance =
                constructor.newInstance(ByteArrayInputStream(bytes, 4, bytes.size - 4), null)
            val unmarshal = cls.getMethod("next")
            return unmarshal.invoke(instance)
        } catch (e: Throwable) {
            Timber.e(e)
            send(ASER().apply {
                errortype = "colfer"
                dataerr = name
            })
        }
        return null
    }
}
