package domain.shadowss.base

import java.lang.ref.WeakReference

class BaseController<V> {

    protected lateinit var viewRef: WeakReference<V>

    fun bind(view: V) {
        viewRef = WeakReference(view)
    }

    fun unbind() {
        viewRef.clear()
    }
}