package domain.shadowss.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.scottyab.rootbeer.RootBeer
import defpackage.marsh.*
import domain.shadowss.extension.*
import domain.shadowss.local.Preferences
import domain.shadowss.manager.WebSocketCallback
import domain.shadowss.manager.WebSocketManager
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.powerManager
import org.jetbrains.anko.toast
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

    @Volatile
    protected var hash: String? = null

    protected fun checkHash(value: String?) = hash == value

    protected fun nextHash(value: String? = null) {
        hash = value
    }

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
                    toast("Let app always run in background")
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

    fun checkRoot(context: Context): Boolean {
        return !RootBeer(context).isRootedWithoutBusyBoxCheck
    }

    fun checkSim(): Boolean {
        return false
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
            REQUEST_PERMISSIONS, REQUEST_ZONE_MODE -> {
                checkRights(reference.get()?.context ?: return)
            }
        }
    }

    open fun release() {
        disposable.dispose()
        reference.clear()
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