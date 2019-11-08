package defpackage

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

fun OkHttpClient.Builder.noopInit() {
    addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
        Timber.tag("NETWORK")
            .d(message)
    }).apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    addNetworkInterceptor(StethoInterceptor())
}