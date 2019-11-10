package domain.shadowss

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Handler
import defpackage.noopInit
import domain.shadowss.extension.isOreoPlus
import domain.shadowss.local.localModule
import domain.shadowss.manager.managerModule
import domain.shadowss.remote.remoteModule
import domain.shadowss.service.ApiService
import org.jetbrains.anko.notificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import timber.log.Timber

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Context>() with provider {
            applicationContext
        }

        import(localModule)

        import(remoteModule)

        import(managerModule)
    }

    private val serviceRunnable = object : Runnable {

        override fun run() {
            try {
                ApiService.start(applicationContext)
            } catch (e: Throwable) {
                Timber.e(e)
                Handler().postDelayed(this, 3000)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        noopInit()
        if (isOreoPlus()) {
            notificationManager.createNotificationChannel(
                NotificationChannel("low", "Low", NotificationManager.IMPORTANCE_LOW).also {
                    it.lockscreenVisibility = Notification.VISIBILITY_SECRET
                }
            )
        }
        serviceRunnable.run()
    }
}