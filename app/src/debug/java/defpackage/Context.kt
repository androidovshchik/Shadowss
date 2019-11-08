package defpackage

import android.content.Context
import com.facebook.stetho.Stetho
import timber.log.Timber

fun Context.noopInit() {
    Timber.plant(Timber.DebugTree())
    Stetho.initialize(
        Stetho.newInitializerBuilder(applicationContext)
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
            .build()
    )
}