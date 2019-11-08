package domain.shadowss.manager

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance

val managerModule = Kodein.Module("manager") {

    bind<LanguageManager>() with eagerSingleton {
        LanguageManager(instance())
    }
}