package domain.shadowss.controller

import defpackage.marsh.SAPO
import defpackage.marsh.SARR
import defpackage.marsh.SARV
import domain.shadowss.screen.StartView

class StartController(referent: StartView) : Controller<StartView>(referent) {

    fun onChoice(isDriver: Boolean) {
        if (checkRights()) {

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

    }

    companion object {

        private val MAX_ATTEMPTS = 4
    }
}