package domain.shadowss.controller

import android.content.Context
import domain.shadowss.screen.BaseView
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

@Suppress("MemberVisibilityCanBePrivate")
open class BaseController<V : BaseView>(view: V) : KodeinAware {

    override val kodein by closestKodein(view as Context)

    override val kodeinContext = kcontext(view as Context)

    protected val reference = WeakReference(view)

    protected val disposable = CompositeDisposable()

    @OverridingMethodsMustInvokeSuper
    open fun release() {
        disposable.dispose()
        reference.clear()
    }
}