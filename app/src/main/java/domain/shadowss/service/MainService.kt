package domain.shadowss.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import domain.shadowss.R
import domain.shadowss.controller.MainController
import domain.shadowss.extension.isRunning
import domain.shadowss.extension.startForegroundService
import domain.shadowss.manager.WebSocketManager
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.startService
import org.jetbrains.anko.stopService
import org.kodein.di.generic.instance
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

interface MainWorker

class ServerService : BaseService(), MainWorker {

    private val controller: MainController by instance()

    private val socketManager: WebSocketManager by instance()

    private var timer: ScheduledFuture<*>? = null

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
        controller.start()
        val executor = Executors.newScheduledThreadPool(1)
        timer = executor.scheduleAtFixedRate({
            if (keepConnection) {
                if (controller.checkInternet(applicationContext)) {
                    socketManager.reconnect()
                }
            } else {
                socketManager.disconnect()
            }
        }, 0L, 2000L, TimeUnit.MILLISECONDS)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.hasExtra(EXTRA_CONNECT)) {
                keepConnection = it.getBooleanExtra(EXTRA_CONNECT, false)
            }
            if (it.hasExtra(EXTRA_LOCATION)) {
                requestLocation = it.getBooleanExtra(EXTRA_LOCATION, false)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        controller.release()
        timer?.cancel(true)
        releaseWakeLock()
        super.onDestroy()
    }

    companion object {

        @Volatile
        private var keepConnection = true

        @Volatile
        private var requestLocation = false

        const val EXTRA_CONNECT = "connect"

        const val EXTRA_LOCATION = "location"

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