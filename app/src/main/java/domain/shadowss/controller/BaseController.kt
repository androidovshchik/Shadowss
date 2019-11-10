package domain.shadowss.controller

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext
import java.lang.ref.WeakReference
import javax.annotation.OverridingMethodsMustInvokeSuper

@Suppress("MemberVisibilityCanBePrivate")
open class BaseController<V>(context: Context) : KodeinAware {

    override val kodein by closestKodein(context)

    override val kodeinContext = kcontext(context)

    protected lateinit var reference: WeakReference<V>

    protected val disposable = CompositeDisposable()

    @Suppress("UNCHECKED_CAST")
    @OverridingMethodsMustInvokeSuper
    open fun bind(view: Any) {
        reference = WeakReference(view as V)
    }

    @OverridingMethodsMustInvokeSuper
    open fun unbind() {
        disposable.dispose()
        reference.clear()
    }
}