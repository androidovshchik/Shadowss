package domain.shadowss.controller

import domain.shadowss.screen.*
import domain.shadowss.service.ServerService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.contexted
import org.kodein.di.generic.provider

val controllerModule = Kodein.Module("controller") {

    bind<MainController>() with contexted<ServerService>().provider {
        MainController(context)
    }

    bind<DriverController>() with contexted<DriverActivity>().provider {
        DriverController(context)
    }

    bind<ManagerController>() with contexted<ManagerActivity>().provider {
        ManagerController(context)
    }

    bind<RegistrationController>() with contexted<RegistrationActivity>().provider {
        RegistrationController(context)
    }

    bind<StartController>() with contexted<StartActivity>().provider {
        StartController(context)
    }

    bind<TermsController>() with contexted<TermsActivity>().provider {
        TermsController(context)
    }
}