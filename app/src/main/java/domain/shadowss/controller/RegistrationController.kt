package domain.shadowss.controller

import android.content.Context
import android.os.Handler
import defpackage.marsh.ASER
import defpackage.marsh.ASPI
import defpackage.marsh.SAPO
import domain.shadowss.extension.isConnected
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.RegistrationView
import org.jetbrains.anko.connectivityManager
import org.kodein.di.generic.instance
import timber.log.Timber

class RegistrationController(referent: RegistrationView) : Controller<RegistrationView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    @Volatile
    private var attempts = 0

    // ui thread
    private val aspiRunnable = object : Runnable {

        override fun run() {
            checkProgress("aspi") {
                if (attempts < MAX_ATTEMPTS) {
                    attempts++
                    socketManager.send(ASPI().apply {
                        rnd = random
                    })
                    Handler().postDelayed(this, 2000)
                    true
                } else {
                    onError("[[MSG,0009]]")
                }
            }
        }
    }

    fun onContinue(context: Context): Boolean {
        return checkProgress(null) {
            state = "none"
            attempts = 0
            if (checkRoot(context)) {
                if (checkRights(context)) {
                    if (context.connectivityManager.isConnected) {
                        nextProgress("aspi")
                        aspiRunnable.run()
                        true
                    } else {
                        onError("[[MSG,0002]]")
                    }
                } else {
                    onError("[[MSG,0000]]")
                }
            } else {
                socketManager.send(ASER().apply {
                    errortype = "root"
                })
                onError("[[MSG,0005]]")
            }
        }
    }

    override fun onSAPO(instance: SAPO) {
        checkProgress("aspi", instance.rnd) {
            state = "sim"
            val error = multiSimManager.updateInfo()
            val slots = multiSimManager.getSlots()
            synchronized(slots) {
                slots.forEach {
                    Timber.i(it.toString())
                }
                if (error != null || slots.isEmpty() || slots.none { it.isActive }) {
                    socketManager.apply {
                        if (error != null) {
                            send(ASER().apply {
                                errortype = "sim"
                                dataerr = error
                            })
                        }
                        send(ASER().apply {
                            errortype = "sim"
                            dataerr = multiSimManager.allFields
                        })
                        send(ASER().apply {
                            errortype = "sim"
                            dataerr = multiSimManager.allMethods
                        })
                    }
                    onError("[[MSG,0006]]")
                    return
                }
                if (slots.none { it.getMCC()?.length == 3 }) {
                    socketManager.send(ASER().apply {
                        errortype = "mcc"
                        dataerr = "${slots[0].getMCC()},${slots.getOrNull(1)?.getMCC()}"
                    })
                    onError("[[MSG,0007]]")
                    return
                }
            }
            reference.get()?.onWait()
            true
        }
    }

    override fun stop() {}

    private fun onError(data: String, instance: Any? = null): Boolean {
        reference.get()?.onError(data, instance)
        resetProgress()
        return false
    }

    companion object {

        private const val MAX_ATTEMPTS = 4
    }
}