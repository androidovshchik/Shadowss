package domain.shadowss.manager

import org.kodein.di.Kodein
import org.kodein.di.generic.*

val managerModule = Kodein.Module("manager") {

    bind<MultiSimManager>() with provider {
        MultiSimManager(instance())
    }

    bind<WebSocketManager>() with singleton {
        WebSocketManager(instance())
    }

    bind<LocationManager>() with singleton {
        LocationManager(instance())
    }

    bind<LanguageManager>() with eagerSingleton {
        LanguageManager(instance())
    }
}