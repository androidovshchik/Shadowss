package domain.shadowss.extensions

import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

fun Context.init() {
    Timber.plant(Timber.DebugTree())
    Stetho.initialize(
        Stetho.newInitializerBuilder(applicationContext)
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
            .build()
    )
}

fun OkHttpClient.Builder.init() {
    addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
        Timber.tag("NETWORK")
            .d(message)
    }).apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    addNetworkInterceptor(StethoInterceptor())
}