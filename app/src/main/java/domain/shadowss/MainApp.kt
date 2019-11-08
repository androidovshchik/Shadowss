package domain.shadowss

import android.app.Application
import domain.shadowss.data.local.localModule
import domain.shadowss.data.remote.remoteModule
import domain.shadowss.extension.noopInit
import domain.shadowss.manager.managerModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        import(localModule)

        import(remoteModule)

        import(managerModule)
    }

    override fun onCreate() {
        super.onCreate()
        noopInit()
    }
}