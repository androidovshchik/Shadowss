package domain.shadowss

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.scottyab.rootbeer.RootBeer
import defpackage.noopInit
import domain.shadowss.controller.controllerModule
import domain.shadowss.extension.isOreoPlus
import domain.shadowss.local.localModule
import domain.shadowss.manager.managerModule
import domain.shadowss.service.ForegroundRunnable
import org.jetbrains.anko.notificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Context>() with provider {
            applicationContext
        }

        import(localModule)

        import(managerModule)

        import(controllerModule)
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
        ForegroundRunnable(applicationContext).run()
        isRooted = RootBeer(applicationContext).isRootedWithoutBusyBoxCheck
    }

    companion object {

        @Volatile
        var isRooted = false
    }
}