package domain.shadowss.remote

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import defpackage.noopInit
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

private const val WSS_URL = "ws://8081.ru"

val remoteModule = Kodein.Module("remote") {

    bind<OkHttpClient>() with provider {
        OkHttpClient.Builder().apply {
            noopInit()
        }.build()
    }

    bind<WebSocketApi>() with singleton {
        Scarlet.Builder()
            .webSocketFactory((instance() as OkHttpClient).newWebSocketFactory(WSS_URL))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()
            .create<WebSocketApi>()
    }
}