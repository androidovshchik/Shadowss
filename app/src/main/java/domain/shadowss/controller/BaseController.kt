package domain.shadowss.controller

import android.content.Context
import defpackage.marsh.*
import domain.shadowss.manager.WebSocketCallback
import domain.shadowss.manager.WebSocketManager
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

typealias Controller<T> = BaseController<T>

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseController<V>(referent: V) : KodeinAware, WebSocketCallback {

    override val kodein by closestKodein(referent as Context)

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

    open fun callback(requestCode: Int, resultCode: Int = 0) {}

    @OverridingMethodsMustInvokeSuper
    open fun release() {
        disposable.dispose()
        reference.clear()
    }
}