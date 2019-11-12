package domain.shadowss.screen

import android.app.Activity
import domain.shadowss.screen.adapter.LogAdapter
import domain.shadowss.screen.dialog.ErrorDialog
import domain.shadowss.screen.dialog.OverflowDialog
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.contexted
import org.kodein.di.generic.provider

val screenModule = Kodein.Module("screen") {

    bind<ErrorDialog>() with contexted<Activity>().provider {
        ErrorDialog(context)
    }

    bind<OverflowDialog>() with contexted<Activity>().provider {
        OverflowDialog(context)
    }

    bind<LogAdapter>() with provider {
        LogAdapter()
    }
}