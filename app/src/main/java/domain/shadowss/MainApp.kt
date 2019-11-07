package domain.shadowss

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import domain.shadowss.extension.noopInit
import domain.shadowss.local.Database
import domain.shadowss.local.Preferences
import domain.shadowss.remote.WssApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Preferences>() with provider {
            Preferences(applicationContext)
        }

        bind<OkHttpClient>() with singleton {
            OkHttpClient.Builder().apply {
                noopInit()
            }.build()
        }

        bind<WssApi>() with singleton {
            Scarlet.Builder()
                .webSocketFactory((instance() as OkHttpClient).newWebSocketFactory("ws://8081.ru"))
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .build()
                .create<WssApi>()
        }

        bind<Database>() with singleton {
            Room.databaseBuilder(applicationContext, Database::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        noopInit()
    }
}