package domain.shadowss.remote

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface WssApi {

    @Receive
    fun observeText(): Flowable<String>

    @Send
    fun sendText(message: String): Boolean
}
