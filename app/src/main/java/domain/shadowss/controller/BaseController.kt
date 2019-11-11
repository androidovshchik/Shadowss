package domain.shadowss.controller

import android.content.Context
import defpackage.marsh.*
import domain.shadowss.manager.WebSocketManager
import domain.shadowss.screen.BaseView
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

@Suppress("MemberVisibilityCanBePrivate")
open class BaseController<V : BaseView>(view: V) : KodeinAware {

    override val kodein by closestKodein(view as Context)

    protected val reference = WeakReference(view)

    protected val disposable = CompositeDisposable()

    protected val socketManager: WebSocketManager by instance()

    open fun start() {
        disposable.add(socketManager.observer
            .subscribe({ instance ->
                reference.get()?.let {
                    when (instance) {
                        is SAPI -> it.onSAPI(instance)
                        is SAPO -> it.onSAPO(instance)
                        is SARM -> it.onSARM(instance)
                        is SARR -> it.onSARR(instance)
                        is SARV -> it.onSARV(instance)
                        is SCNG -> it.onSCNG(instance)
                        is SMNG -> it.onSMNG(instance)
                    }
                }
            }, {
                Timber.e(it)
            })
        )
    }

    open fun stop() {
        disposable.clear()
    }

    @OverridingMethodsMustInvokeSuper
    open fun release() {
        disposable.dispose()
        reference.clear()
    }
}