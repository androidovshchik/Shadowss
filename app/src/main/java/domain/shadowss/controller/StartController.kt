package domain.shadowss.controller

import android.content.Context
import android.os.Handler
import defpackage.marsh.ASPI
import defpackage.marsh.SAPO
import defpackage.marsh.SARR
import defpackage.marsh.SARV
import domain.shadowss.local.Preferences
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.StartView
import org.kodein.di.generic.instance

class StartController(referent: StartView) : Controller<StartView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    private val preferences: Preferences by instance()

    @Volatile
    private var random = 0

    @Volatile
    private var attempts = 0

    private val handler = Handler()

    private val closableRunnable = Runnable {
        random = (1..99).random()
        socketManager.send(ASPI().apply {
            rnd = random.toShort()
        })
    }

    fun onChoice(context: Context): Boolean {
        return if (checkState(null)) {
            nextState("start_none")
            if (checkRights(context)) {
                if (checkAgree(preferences)) {
                    if (checkRoot(context)) {
                        if (checkInternet(context.applicationContext)) {
                            nextState("start_aspi")
                            closableRunnable.run()
                            true
                        } else {
                            reference.get()?.onError("[[MSG,0002]]")
                            nextState(null)
                            false
                        }
                    } else {
                        reference.get()?.onError("[[MSG,0005]]")
                        nextState(null)
                        false
                    }
                } else {
                    reference.get()?.onError("[[MSG,0001]]")
                    nextState(null)
                    false
                }
            } else {
                reference.get()?.onError("[[MSG,0000]]")
                nextState(null)
                false
            }
        } else {
            false
        }
        /*disposable.add(Observable.just(true)
            .observeOn(Schedulers.io())
            .subscribe({
                multiSimManager.updateInfo()
                val slots = multiSimManager.getSlots()
                synchronized(slots) {
                    slots.forEach {
                        Timber.e(it.toString())
                    }
                    if (slots.isEmpty()) {
                        Timber.e(multiSimManager.allMethodsAndFields)
                    }
                }
            }, {
                Timber.e(it)
            })
        )*/
    }

    override fun onSAPO(instance: SAPO) {
        if (checkState("start_aspi")) {
            if (random == instance.rnd.toInt()) {
                handler.removeCallbacks(closableRunnable)
                if () {

                }
            }
        }
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