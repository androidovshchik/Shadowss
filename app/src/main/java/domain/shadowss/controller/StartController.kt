package domain.shadowss.controller

import android.content.Context
import android.os.Handler
import defpackage.marsh.*
import domain.shadowss.MainApp
import domain.shadowss.extension.isConnected
import domain.shadowss.local.Preferences
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.StartView
import io.reactivex.Single
import org.jetbrains.anko.connectivityManager
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.concurrent.TimeUnit

class StartController(referent: StartView) : Controller<StartView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    private val preferences: Preferences by instance()

    @Volatile
    private var attempts = 0

    // ui thread
    private val aspiRunnable = object : Runnable {

        override fun run() {
            checkProgress("start_aspi") {
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

    fun onChoice(context: Context): Boolean {
        return checkProgress(null) {
            state = "start_none"
            attempts = 0
            if (!MainApp.isRooted) {
                if (checkRights(context)) {
                    if (preferences.agree) {
                        if (context.connectivityManager.isConnected) {
                            nextProgress("start_aspi")
                            aspiRunnable.run()
                            true
                        } else {
                            onError("[[MSG,0002]]")
                        }
                    } else {
                        onError("[[MSG,0001]]")
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
        checkProgress("start_aspi", instance.rnd) {
            socketManager.send(ASRV().apply {
                rnd = nextProgress("start_asrv")
            })
            disposable.add(Single.just(true)
                .delay(5000L, TimeUnit.MILLISECONDS)
                .subscribe({
                    checkProgress("start_asrv") {
                        onError("[[MSG,0009]]")
                    }
                }, {
                    Timber.e(it)
                })
            )
            true
        }
    }

    override fun onSARV(instance: SARV) {
        checkProgress("start_asrv", instance.rnd) {
            when (instance.error) {
                "0" -> {
                    state = "start_sim"
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
                        socketManager.send(ASRR().apply {
                            rnd = nextProgress("start_asrr")
                            mcc1 = slots[0].getMCC().toString()
                            mcc2 = slots.getOrNull(1)?.getMCC().toString()
                            lang = slots[0].simCountryIso.toString()
                        })
                    }
                    disposable.add(
                        Single.just(true)
                            .delay(5000L, TimeUnit.MILLISECONDS)
                            .subscribe({
                                checkProgress("start_asrr") {
                                    onError("[[MSG,0009]]")
                                }
                            }, {
                                Timber.e(it)
                            })
                    )
                    true
                }
                "0010" -> {
                    onError("[[MSG,0010]]", instance)
                }
                else -> {
                    onError(instance.error)
                }
            }
        }
    }

    override fun onSARR(instance: SARR) {
        checkProgress("start_asrr", instance.rnd) {
            when (instance.error) {
                "0" -> {
                    resetProgress()
                    reference.get()?.onSuccess(instance.data)
                    true
                }
                "0008" -> {
                    onError("[[MSG,0008]]", instance)
                }
                else -> {
                    onError(instance.error)
                }
            }
        }
    }

    private fun onError(data: String, instance: Any? = null): Boolean {
        reference.get()?.onError(data, instance)
        resetProgress()
        return false
    }

    companion object {

        private const val MAX_ATTEMPTS = 4
    }
}