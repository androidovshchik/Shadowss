package domain.shadowss.remote

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send

interface WssApi {

    @Receive
    fun observeText(): Flowable<String>

    @Send
    fun sendText(message: String): Boolean
}
