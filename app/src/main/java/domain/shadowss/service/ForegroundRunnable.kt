package domain.shadowss.service

import android.content.Context
import android.os.Handler
import timber.log.Timber
import java.lang.ref.WeakReference

class ForegroundRunnable(context: Context) : Runnable {

    private val reference = WeakReference(context)

    init {
        run()
    }

    override fun run() {
        try {
            ApiService.start(reference.get() ?: return)
        } catch (e: Throwable) {
            Timber.e(e)
            Handler().postDelayed(this, 3000)
        }
    }
}