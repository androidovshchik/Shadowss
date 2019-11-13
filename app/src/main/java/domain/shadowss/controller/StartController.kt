package domain.shadowss.controller

import defpackage.marsh.ASPI
import defpackage.marsh.SAPO
import defpackage.marsh.SARR
import defpackage.marsh.SARV
import domain.shadowss.local.Preferences
import domain.shadowss.screen.StartView
import org.kodein.di.generic.instance

class StartController(referent: StartView) : Controller<StartView>(referent) {

    private val preferences: Preferences by instance()

    @Volatile
    private var step = 0

    @Volatile
    private var random = 0

    @Volatile
    private var attempts = 0

    fun onChoice(isDriver: Boolean) {
        step = 0
        if (checkRights()) {
            if (checkAgree(preferences)) {
                if (checkInternet()) {
                    random = (1..99).random()
                    socketManager.send(ASPI().apply {
                        rnd = random.toShort()
                    })
                }
            }
        } else {

        }
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
                reference.get()?.onSuccess()
            }
            "0010" -> {
                reference.get()?.onError()
            }
            else -> {
                reference.get()?.onError()
            }
        }
    }

    override fun callback(requestCode: Int, resultCode: Int) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {

            }
            REQUEST_ZONE_MODE -> {

            }
        }
    }

    companion object {

        private val MAX_ATTEMPTS = 4
    }
}