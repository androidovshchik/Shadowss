package domain.shadowss.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.scottyab.rootbeer.RootBeer
import defpackage.marsh.*
import domain.shadowss.extension.*
import domain.shadowss.local.Preferences
import domain.shadowss.manager.WebSocketCallback
import domain.shadowss.manager.WebSocketManager
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.powerManager
import org.jetbrains.anko.telephonyManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

typealias Controller<T> = BaseController<T>

interface ControllerReference {

    val context: Context
}

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseController<R : ControllerReference>(referent: R) : KodeinAware,
    WebSocketCallback {

    override val kodein by closestKodein(referent.context)

    protected val reference = WeakReference(referent)

    protected val disposable = CompositeDisposable()

    protected val socketManager: WebSocketManager by instance()

    open fun start() {
        disposable.add(socketManager.observer
            .subscribe({ instance ->
                when (instance) {
                    is SAPI -> onSAPI(instance)
                    is SAPO -> onSAPO(instance)
                    is SARM -> onSARM(instance)
                    is SARR -> onSARR(instance)
                    is SARV -> onSARV(instance)
                    is SCNG -> onSCNG(instance)
                    is SMNG -> onSMNG(instance)
                }
            }, {
                Timber.e(it)
            })
        )
    }

    fun checkRights(context: Context): Boolean = context.run {
        var granted: Boolean
        if (isMarshmallowPlus()) {
            val permissions = if (isOreoPlus()) {
                arrayOf(Manifest.permission.ANSWER_PHONE_CALLS) + DANGER_PERMISSIONS
            } else {
                DANGER_PERMISSIONS
            }
            granted = areGranted(*permissions)
            if (!granted) {
                if (this is Activity) {
                    requestPermissions(REQUEST_PERMISSIONS, *permissions)
                }
                return@run false
            }
            granted = powerManager.isIgnoringBatteryOptimizations(packageName)
            if (!granted) {
                if (this is Activity) {
                    startActivityForResult(
                        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
                        REQUEST_ZONE_MODE
                    )
                }
                return@run false
            }
        }
        return@run true
    }

    fun checkInternet(context: Context): Boolean {
        return context.connectivityManager.isConnected
    }

    fun checkAgree(preferences: Preferences): Boolean {
        return preferences.agree
    }

    fun wssregconnect() {

    }

    fun checkuid() {

    }

    fun checkRoot(context: Context): Boolean {
        return !RootBeer(context).isRootedWithoutBusyBoxCheck
    }

    fun checksim() {

    }

    @SuppressLint("MissingPermission")
    fun checkMcc(context: Context): Pair<String?, String?> = context.run {
        if (isLollipopMR1Plus()) {
            val subscriptionManager =
                getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            if (areGranted(Manifest.permission.READ_PHONE_STATE)) {
                val infoList = subscriptionManager.activeSubscriptionInfoList
                val mcc1 = infoList.getOrNull(0)?.mcc.toString()
                val mcc2 = infoList.getOrNull(1)?.mcc.toString()
                return@run mcc1 to mcc2
            }
        } else {
            val code = telephonyManager.networkOperator
            if (!TextUtils.isEmpty(code)) {
                val length = 3
                if (code.length >= length) {
                    mcc1 = code.substring(0, length)
                }
            }
        }
        return@run null to null
    }

    fun checkRegion() {
        socketManager.send(ASRR().apply {

        })
    }

    fun checkVersion() {
        socketManager.send(ASRV().apply {

        })
    }

    fun checkMobile() {
        /*val smsQuery = RxCursorLoader.Query.Builder()
            .setContentUri(Uri.parse("content://sms"))
            .setSortOrder("${Telephony.Sms.DATE} DESC LIMIT 5")
            .create()
        disposable.add(RxCursorLoader.observable(contentResolver, smsQuery, Schedulers.io())
            .subscribe({ cursor ->
                synchronized(this) {
                    cursor.use {
                        Timber.i("content://sms ${cursor.count}")
                        if (cursor.moveToLast()) {
                            do {
                                try {
                                    readSms(it)
                                } catch (e: Throwable) {
                                    Timber.e(e)
                                }
                            } while (cursor.moveToPrevious())
                        }
                    }
                }
            }, {
                Timber.e(it)
            }))*/
    }

    override fun onSAPI(instance: SAPI) {}

    override fun onSAPO(instance: SAPO) {}

    override fun onSARM(instance: SARM) {}

    override fun onSARR(instance: SARR) {}

    override fun onSARV(instance: SARV) {}

    override fun onSCNG(instance: SCNG) {}

    override fun onSMNG(instance: SMNG) {}

    open fun stop() {
        disposable.clear()
    }

    @OverridingMethodsMustInvokeSuper
    open fun callback(requestCode: Int, resultCode: Int = 0) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                checkRights(reference.get()?.context ?: return)
            }
        }
    }

    open fun release() {
        disposable.dispose()
        reference.clear()
    }

    private val Context.isSIM2Ready: Boolean
        get() = try {
            val state =
                invokeBySlot("getSimState", 1)?.toInt() ?: TelephonyManager.SIM_STATE_UNKNOWN
            state != TelephonyManager.SIM_STATE_ABSENT && state != TelephonyManager.SIM_STATE_UNKNOWN
        } catch (e: Throwable) {
            Timber.e(e)
            true
        }

    private fun Context.invokeBySlot(name: String, slotId: Int): String? {
        return try {
            telephonyManager.run {
                val method = javaClass.getMethod(name, Int::class.java)
                method.invoke(this, slotId) as? String
            }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    companion object {

        const val REQUEST_PERMISSIONS = 200

        const val REQUEST_ZONE_MODE = 300

        private val DANGER_PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}