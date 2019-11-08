package domain.shadowss.screen.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

interface AdapterListener<T> {

    fun onAdapterEvent(position: Int, item: T, event: Any? = null)
}

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    val items = arrayListOf<T>()

    protected var reference: WeakReference<AdapterListener<T>>? = null

    /**
     * It is assumed that this will be called one time or never
     */
    fun setListener(listener: AdapterListener<T>) {
        reference = WeakReference(listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBindItem(position, items[position])
    }

    override fun getItemCount() = items.size
}

@Suppress("UNUSED_PARAMETER", "unused")
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBindItem(position: Int, item: T)

    val appContext: Context
        get() = itemView.context.applicationContext
}