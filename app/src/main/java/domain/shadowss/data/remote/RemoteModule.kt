package domain.shadowss.data.remote

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import domain.shadowss.extension.noopInit
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val remoteModule = Kodein.Module("remote") {

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
}