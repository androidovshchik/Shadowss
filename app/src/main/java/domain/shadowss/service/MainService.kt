package domain.shadowss.service

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import domain.shadowss.R
import domain.shadowss.controller.MainController
import domain.shadowss.extension.isConnected
import domain.shadowss.extension.isRunning
import domain.shadowss.extension.startForegroundService
import domain.shadowss.manager.SocketManager
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.startService
import org.jetbrains.anko.stopService
import org.kodein.di.generic.instance
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

interface MainWorker : BaseWorker

class MainService : BaseService(), MainWorker {

    private val controller: MainController by instance()

    private val socketManager: SocketManager by instance()

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
            socketManager.apply {
                if (connectivityManager.isConnected) {
                    reconnect()
                } else {
                    disconnect()
                }
            }
        }, 0L, 20_000L, TimeUnit.MILLISECONDS)
    }

    override fun onDestroy() {
        controller.release()
        timer?.cancel(true)
        releaseWakeLock()
        super.onDestroy()
    }

    companion object {

        @Throws(Throwable::class)
        fun start(context: Context, vararg params: Pair<String, Any?>): Boolean = context.run {
            return if (!activityManager.isRunning<MainService>()) {
                startForegroundService<MainService>(*params) != null
            } else {
                startService<MainService>(*params) != null
            }
        }

        @Suppress("unused")
        private fun stop(context: Context): Boolean = context.run {
            if (activityManager.isRunning<MainService>()) {
                return stopService<MainService>()
            }
            return true
        }
    }
}