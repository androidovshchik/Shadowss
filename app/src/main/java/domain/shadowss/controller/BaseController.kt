package domain.shadowss.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import defpackage.marsh.*
import domain.shadowss.extension.areGranted
import domain.shadowss.extension.isMarshmallowPlus
import domain.shadowss.extension.isOreoPlus
import domain.shadowss.extension.requestPermissions
import domain.shadowss.manager.WebSocketCallback
import domain.shadowss.manager.WebSocketManager
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.longToast
import org.jetbrains.anko.powerManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

typealias Controller<T> = BaseController<T>

interface ControllerReference

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseController<R : ControllerReference>(referent: R) : KodeinAware,
    WebSocketCallback {

    override val kodein by closestKodein(referent as Context)

    protected val reference = WeakReference(referent)

    protected val disposable = CompositeDisposable()

    protected val socketManager: WebSocketManager by instance()

    @Volatile
    protected var random: Short = 0

    @Volatile
    protected var state: String? = null

    init {
        nextRandom()
    }

    protected fun checkRandom(number: Short) = random == number

    protected fun nextRandom(): Short {
        random = (1..32_000).random().toShort()
        return random
    }

    protected inline fun checkProgress(value: String?, block: () -> Boolean): Boolean {
        return if (state == value) {
            block()
        } else {
            false
        }
    }

    protected inline fun checkProgress(
        value: String,
        number: Short,
        block: () -> Boolean
    ): Boolean {
        return if (checkRandom(number)) {
            checkProgress(value, block)
        } else {
            false
        }
    }

    fun nextProgress(value: String?): Short {
        state = value
        return nextRandom()
    }

    fun resetProgress() {
        nextProgress(null)
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
                return false
            }
            granted = powerManager.isIgnoringBatteryOptimizations(packageName)
            if (!granted) {
                if (this is Activity) {
                    startActivityForResult(
                        Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
                        REQUEST_ZONE_MODE
                    )
                    longToast("Let app always run in background")
                }
                return false
            }
        }
        true
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
    open fun callback(context: Context, requestCode: Int, resultCode: Int = 0) {
        when (requestCode) {
            REQUEST_PERMISSIONS, REQUEST_ZONE_MODE -> {
                checkRights(context)
            }
        }
    }

    open fun release() {
        resetProgress()
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