package domain.shadowss

import android.app.Application
import android.content.Context
import defpackage.noopInit
import domain.shadowss.data.local.localModule
import domain.shadowss.data.remote.remoteModule
import domain.shadowss.manager.managerModule
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

        import(remoteModule)

        import(managerModule)
    }

    override fun onCreate() {
        super.onCreate()
        noopInit()
    }
}