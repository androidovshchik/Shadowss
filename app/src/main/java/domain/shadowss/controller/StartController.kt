package domain.shadowss.controller

import defpackage.marsh.SAPO
import defpackage.marsh.SARR
import defpackage.marsh.SARV
import domain.shadowss.screen.StartView

class StartController(referent: StartView) : Controller<StartView>(referent) {

    fun onChoice(isDriver: Boolean) {

    }

    override fun onSAPO(instance: SAPO) {

    }

    override fun onSARR(instance: SARR) {

    }

    override fun onSARV(instance: SARV) {
        when (instance.error) {
            "0" -> {

            }
            "0010" -> {

            }
            else -> {

            }
        }
    }
}