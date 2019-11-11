package domain.shadowss.manager

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val managerModule = Kodein.Module("manager") {

    bind<MarshManager>() with provider {
        MarshManager()
    }

    bind<LanguageManager>() with eagerSingleton {
        LanguageManager(instance())
    }
}

interface Manager {

    fun init(vararg args: Any?)

    fun release(vararg args: Any?)
}