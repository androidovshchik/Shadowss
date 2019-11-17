package domain.shadowss.controller

import android.content.Context
import android.os.Handler
import defpackage.marsh.*
import domain.shadowss.MainApp
import domain.shadowss.extension.isConnected
import domain.shadowss.local.Preferences
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.StartView
import org.jetbrains.anko.connectivityManager
import org.kodein.di.generic.instance
import timber.log.Timber

class StartController(referent: StartView) : Controller<StartView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    private val preferences: Preferences by instance()

    @Volatile
    private var random = 0

    @Volatile
    private var attempts = 0

    private val aspiRunnable = object : Runnable {

        override fun run() {
            if (checkState("start_aspi")) {
                if (attempts < MAX_ATTEMPTS) {
                    attempts++
                    random = (1..99).random()
                    socketManager.send(ASPI().apply {
                        rnd = random.toShort()
                    })
                    Handler().postDelayed(this, 2000)
                } else {
                    reference.get()?.onError("[[MSG,0009]]")
                }
            }
        }
    }

    fun onChoice(context: Context): Boolean {
        return if (checkState(null)) {
            nextState("start_none")
            attempts = 0
            if (checkRights(context)) {
                if (preferences.agree) {
                    if (!MainApp.isRooted) {
                        if (context.connectivityManager.isConnected) {
                            nextState("start_aspi")
                            aspiRunnable.run()
                            true
                        } else {
                            reference.get()?.onError("[[MSG,0002]]")
                            false
                        }
                    } else {
                        socketManager.send(ASER().apply {
                            errortype = "root"
                        })
                        reference.get()?.onError("[[MSG,0005]]")
                        false
                    }
                } else {
                    reference.get()?.onError("[[MSG,0001]]")
                    false
                }
            } else {
                reference.get()?.onError("[[MSG,0000]]")
                false
            }
        } else {
            false
        }
    }

    override fun onSAPO(instance: SAPO) {
        if (checkState("start_aspi")) {
            if (random == instance.rnd.toInt()) {
                nextState("start_sim")
                val error = multiSimManager.updateInfo()
                val slots = multiSimManager.getSlots()
                synchronized(slots) {
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
                                dataerr = multiSimManager.allMethodsAndFields
                            })
                            send(ASER().apply {
                                errortype = "sim"
                                dataerr = multiSimManager.allMethodsAndFields
                            })
                        }
                        reference.get()?.onError("[[MSG,0006]]")
                        return
                    }
                    if (slots.none { it.getMCC()?.length == 3 }) {
                        socketManager.send(ASER().apply {
                            errortype = "mcc"
                            dataerr = "${slots[0].getMCC()},${slots.getOrNull(1)?.getMCC()}"
                        })
                        reference.get()?.onError("[[MSG,0007]]")
                        return
                    }
                    random = (1..99).random()
                    socketManager.send(ASRV().apply {
                        rnd = random.toShort()
                    })
                    slots.forEach {
                        Timber.e(it.toString())
                    }
                    if (slots.isEmpty()) {
                        Timber.e(multiSimManager.allMethodsAndFields)
                    }
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

    companion object {

        private const val MAX_ATTEMPTS = 4
    }
}