package domain.shadowss.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import domain.shadowss.controller.ControllerReference
import org.jetbrains.anko.powerManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

interface BaseWorker : ControllerReference

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseService : Service(), KodeinAware, BaseWorker {

    override val kodein by closestKodein()

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @SuppressLint("WakelockTimeout")
    protected fun acquireWakeLock(name: String) {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, name).apply {
                acquire()
            }
        }
    }

    protected fun releaseWakeLock() {
        wakeLock?.let {
            it.release()
            wakeLock = null
        }
    }
}
