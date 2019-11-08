package domain.shadowss.screen.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

interface AdapterListener<T> {

    fun onAdapterEvent(position: Int, item: T, event: Any? = null)
}

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseAdapter<I> : RecyclerView.Adapter<BaseViewHolder<I>>() {

    val items = arrayListOf<I>()

    protected var reference: WeakReference<AdapterListener<I>>? = null

    /**
     * It is assumed that this will be called one time or never
     */
    fun setListener(listener: AdapterListener<I>) {
        reference = WeakReference(listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int) {
        holder.onBindItem(position, items[position])
    }

    override fun getItemCount() = items.size
}

@Suppress("UNUSED_PARAMETER", "unused")
abstract class BaseViewHolder<I>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBindItem(position: Int, item: I)

    val appContext: Context
        get() = itemView.context.applicationContext
}