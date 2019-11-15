package domain.shadowss.manager

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val managerModule = Kodein.Module("manager") {

    bind<WebSocketManager>() with singleton {
        WebSocketManager(instance())
    }

    bind<MultiSimManager>() with singleton {
        MultiSimManager(instance())
    }

    bind<LocationManager>() with singleton {
        LocationManager(instance())
    }

    bind<LanguageManager>() with eagerSingleton {
        LanguageManager(instance())
    }
}