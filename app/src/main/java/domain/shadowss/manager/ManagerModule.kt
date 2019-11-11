package domain.shadowss.manager

import org.kodein.di.Kodein
import org.kodein.di.generic.*

val managerModule = Kodein.Module("manager") {

    bind<MarshManager>() with provider {
        MarshManager()
    }

    bind<WebSocketManager>() with singleton {
        WebSocketManager()
    }

    bind<LanguageManager>() with eagerSingleton {
        LanguageManager(instance())
    }
}

interface Manager {

    fun init(vararg args: Any?)

    fun release(vararg args: Any?)
}