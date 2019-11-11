package domain.shadowss.screen

import domain.shadowss.controller.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.contexted
import org.kodein.di.generic.provider

val screenModule = Kodein.Module("screen") {

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