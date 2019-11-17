package domain.shadowss.controller

import android.content.Context
import defpackage.marsh.SAPO
import defpackage.marsh.SARR
import defpackage.marsh.SARV
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.StartView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import timber.log.Timber

class StartController(referent: StartView) : Controller<StartView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    @Volatile
    private var random = 0

    @Volatile
    private var attempts = 0

    fun onChoice(context: Context): Boolean {
        if (!checkHash(null)) {
            return false
        }
        nextHash("start")
        disposable.add(Observable.just(true)
            .observeOn(Schedulers.io())
            .subscribe({
                multiSimManager.updateInfo()
                val slots = multiSimManager.getSlots()
                synchronized(slots) {
                    slots.forEach {
                        Timber.e(it.toString())
                    }
                }
            }, {
                Timber.e(it)
            })
        )
        // multiSimManager.updateSlots()
        /*if (checkRights()) {
            if (checkAgree(preferences)) {
                if (checkInternet()) {
                    random = (1..99).random()
                    socketManager.send(ASPI().apply {
                        rnd = random.toShort()
                    })
                }
            }
        } else {

        }*/
        return false
    }

    override fun onSAPO(instance: SAPO) {

    }

    override fun onSARR(instance: SARR) {
        when (instance.error) {
            "0" -> {

            }
            "0008" -> {

            }
            else -> {

            }
        }
    }

    override fun onSARV(instance: SARV) {
        when (instance.error) {
            "0" -> {
                //reference.get()?.onSuccess()
            }
            "0010" -> {
                //reference.get()?.onError()
            }
            else -> {
                //reference.get()?.onError()
            }
        }
    }

    override fun callback(context: Context, requestCode: Int, resultCode: Int) {
        super.callback(context, requestCode, resultCode)
        when (requestCode) {
            REQUEST_ZONE_MODE -> {

            }
        }
    }

    companion object {

        private val MAX_ATTEMPTS = 4
    }
}