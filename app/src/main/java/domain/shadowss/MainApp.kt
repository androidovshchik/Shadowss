package domain.shadowss

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import domain.shadowss.remote.WssApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import timber.log.Timber

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<OkHttpClient>() with singleton {
            OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                        Timber.tag("NETWORK")
                            .d(message)
                    }).apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    addNetworkInterceptor(StethoInterceptor())
                }
            }.build()
        }

        bind<WssApi>() with singleton {
            Scarlet.Builder()
                .webSocketFactory(instance().newWebSocketFactory("ws://8081.ru"))
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .build()
                .create<WssApi>()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initialize(
                Stetho.newInitializerBuilder(applicationContext)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
                    .build()
            )
        }
    }
}