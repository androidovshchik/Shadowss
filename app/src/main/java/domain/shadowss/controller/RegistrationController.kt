package domain.shadowss.controller

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.loader.content.CursorLoader
import defpackage.marsh.*
import domain.shadowss.extension.isConnected
import domain.shadowss.extension.isPiePlus
import domain.shadowss.manager.MultiSimManager
import domain.shadowss.screen.RegistrationView
import org.jetbrains.anko.connectivityManager
import org.kodein.di.generic.instance
import timber.log.Timber

class CursorCalls(context: Context) : CursorLoader(context) {

    override fun loadInBackground(): Cursor? {
        val calls = Uri.parse("content://call_log/calls")
        return context.contentResolver.query(calls, null, null, null, null)
    }
}

class RegistrationController(referent: RegistrationView) : Controller<RegistrationView>(referent) {

    private val multiSimManager: MultiSimManager by instance()

    @Volatile
    private var attempts = 0

    private val phoneStateListener = object : PhoneStateListener() {

        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    Timber.v("onCallStateChanged: CALL_STATE_IDLE")
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> Timber.v("onCallStateChanged: CALL_STATE_OFFHOOK")
                else -> Timber.v("onCallStateChanged: $state")
            }
        }
    }

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
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
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
                reference.get()?.let {
                    socketManager.send(ASRM().apply {
                        rnd = nextProgress("asrm")
                        Regcode = it.regCode
                        mobile = it.mobilePhone
                        utype = it.userType.toString()
                        mcc1 = slots[0].getMCC().toString()
                        mcc2 = slots.getOrNull(1)?.getMCC().toString()
                        imsi1 = slots[0].imsi.toString()
                        imsi2 = slots.getOrNull(1)?.imsi.toString()
                    })
                }
            }
            reference.get()?.onWait()
            true
        }
    }

    override fun onSARM(instance: SARM) {
        checkProgress("asrm", instance.rnd) {
            when (instance.error) {
                "0" -> {
                    reference.get()?.onSuccess()
                    resetProgress()
                    true
                }
                "0013" -> {
                    onError("[[MSG,0013]]", instance)
                }
                else -> {
                    onError(instance.error)
                }
            }
        }
    }

    override fun stop() {}

    private fun onError(data: String, instance: Any? = null): Boolean {
        reference.get()?.onError(data, instance)
        resetProgress()
        return false
    }

    @SuppressLint("MissingPermission")
    private fun Context.killCall() {
        if (isPiePlus()) {
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            telecomManager.endCall()
        } else {
            try {
                val method = javaClass.getDeclaredMethod("getITelephony")
                method.isAccessible = true
                val iTelephony = method.invoke(this)
                iTelephony.javaClass
                    .getDeclaredMethod("endCall")
                    .invoke(iTelephony)
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    companion object {

        private const val MAX_ATTEMPTS = 4
    }
}