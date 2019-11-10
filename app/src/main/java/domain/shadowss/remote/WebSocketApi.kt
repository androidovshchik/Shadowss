package domain.shadowss.remote

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface WebSocketApi {

    @Receive
    fun observe(): Flowable<ByteArray>

    @Send
    fun send(message: ByteArray): Boolean
}
