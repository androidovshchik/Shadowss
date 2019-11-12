package domain.shadowss.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import defpackage.marsh.*
import domain.shadowss.R
import domain.shadowss.controller.ServerController
import domain.shadowss.extension.isRunning
import domain.shadowss.extension.startForegroundService
import domain.shadowss.manager.WebSocketCallback
import domain.shadowss.manager.WebSocketManager
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.startService
import org.jetbrains.anko.stopService
import org.kodein.di.generic.instance
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ServerService : BaseService(), WebSocketCallback {

    private val controller: ServerController by instance()

    private val socketManager: WebSocketManager by instance()

    private var timer: ScheduledFuture<*>? = null

    @Volatile
    private var keepConnection = false

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        startForeground(
            Int.MAX_VALUE, NotificationCompat.Builder(applicationContext, "low")
                .setSmallIcon(R.drawable.ic_api)
                .setContentTitle("Фоновой сервис")
                .setOngoing(true)
                .build()
        )
        acquireWakeLock(javaClass.name)
        val executor = Executors.newScheduledThreadPool(1)
        timer = executor.scheduleAtFixedRate({
            if (keepConnection) {
                socketManager.reconnect()
            } else {

            }
        }, 0L, 2000L, TimeUnit.MILLISECONDS)
        controller.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.hasExtra(EXTRA_KEEP)) {
                keepConnection = it.getBooleanExtra(EXTRA_KEEP, false)
            }
        }
        return START_STICKY
    }

    override fun onSAPI(instance: SAPI) {}

    override fun onSAPO(instance: SAPO) {}

    override fun onSARM(instance: SARM) {}

    override fun onSARR(instance: SARR) {}

    override fun onSARV(instance: SARV) {}

    override fun onSCNG(instance: SCNG) {}

    override fun onSMNG(instance: SMNG) {}

    override fun onDestroy() {
        controller.release()
        timer?.cancel(true)
        releaseWakeLock()
        super.onDestroy()
    }

    companion object {

        private const val EXTRA_KEEP = "keep"

        @Throws(Throwable::class)
        fun start(context: Context, vararg params: Pair<String, Any?>): Boolean = context.run {
            return if (!activityManager.isRunning<ServerService>()) {
                startForegroundService<ServerService>(*params) != null
            } else {
                startService<ServerService>(*params) != null
            }
        }

        @Suppress("unused")
        private fun stop(context: Context): Boolean = context.run {
            if (activityManager.isRunning<ServerService>()) {
                return stopService<ServerService>()
            }
            return true
        }
    }
}