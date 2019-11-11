package domain.shadowss.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import domain.shadowss.R
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

class ApiService : BaseService() {

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
        val executor = Executors.newScheduledThreadPool(1)
        timer = executor.scheduleAtFixedRate({
            socketManager.reconnect()
        }, 0L, 2000L, TimeUnit.MILLISECONDS)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.hasExtra()) {

            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        timer?.cancel(true)
        releaseWakeLock()
        super.onDestroy()
    }

    companion object {

        private const val EXTRA_CONNECT = 5000L

        @Throws(Throwable::class)
        fun start(context: Context, vararg params: Pair<String, Any?>): Boolean = context.run {
            return if (!activityManager.isRunning<ApiService>()) {
                startForegroundService<ApiService>(*params) != null
            } else {
                startService<ApiService>(*params) != null
            }
        }

        @Suppress("unused")
        private fun stop(context: Context): Boolean = context.run {
            if (activityManager.isRunning<ApiService>()) {
                return stopService<ApiService>()
            }
            return true
        }
    }
}