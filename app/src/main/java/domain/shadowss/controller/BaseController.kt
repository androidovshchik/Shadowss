package domain.shadowss.controller

import android.content.Context
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext
import java.lang.ref.WeakReference

@Suppress("MemberVisibilityCanBePrivate")
open class BaseController<V>(context: Context) : KodeinAware {

    override val kodein by closestKodein(context)

    override val kodeinContext = kcontext(context)

    protected lateinit var viewRef: WeakReference<V>

    fun bind(view: V) {
        viewRef = WeakReference(view)
    }

    fun unbind() {
        viewRef.clear()
    }
}