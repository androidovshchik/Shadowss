package domain.shadowss

import android.app.Application
import com.facebook.stetho.Stetho
import com.tinder.scarlet.Scarlet
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import domain.shadowss.remote.WssApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<WssApi>() with singleton {
            Scarlet.Builder()
                .webSocketFactory(okHttpClient.newWebSocketFactory("ws://8081.ru"))
                .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
                .build()
                .create<WssApi>()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                Stetho.newInitializerBuilder(applicationContext)
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
                    .build()
            )
        }
    }
}