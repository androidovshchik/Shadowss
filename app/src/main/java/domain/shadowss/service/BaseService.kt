package domain.shadowss.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.powerManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

abstract class BaseService : Service(), KodeinAware {

    override val kodein by closestKodein()

    private var wakeLock: PowerManager.WakeLock? = null

    protected val disposable = CompositeDisposable()

    override fun onBind(intent: Intent): IBinder? {
        return null
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

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
