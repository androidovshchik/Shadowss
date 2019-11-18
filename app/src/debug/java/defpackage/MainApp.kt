package defpackage

import android.content.Context
import com.facebook.stetho.Stetho
import leakcanary.AppWatcher
import timber.log.Timber

fun Context.noopInit() {
    Timber.plant(Timber.DebugTree())
    Stetho.initialize(
        Stetho.newInitializerBuilder(applicationContext)
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
            .build()
    )
}

fun watchLeaks(instance: Any) {
    AppWatcher.objectWatcher.watch(instance)
}