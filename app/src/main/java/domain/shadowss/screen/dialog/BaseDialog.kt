@file:Suppress("DEPRECATION", "unused")

package domain.shadowss.screen.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import domain.shadowss.extension.activity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import java.lang.ref.WeakReference

private typealias L = DialogListener

private interface DialogListener : DialogInterface.OnShowListener, DialogInterface.OnDismissListener

private class DialogObserver(listener: L) : L {

    private val reference = WeakReference(listener)

    override fun onShow(dialog: DialogInterface?) {
        reference.get()?.onShow(dialog)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        reference.get()?.onDismiss(dialog)
    }
}

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseDialog(activity: Activity, id: Int) : Dialog(activity, id), KodeinAware, L {

    override val kodein by closestKodein(activity)

    protected open val shouldBeClosable = true

    protected var isClosable = false

    private val observer = DialogObserver(this)

    private val handler = Handler()

    private val closableRunnable = Runnable {
        setCancelable(true)
        isClosable = true
        onClosableState()
    }

    init {
        setCancelable(false)
        setOnShowListener(observer)
        setOnDismissListener(observer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onShow(dialog: DialogInterface?) {
        setCancelable(false)
        if (shouldBeClosable) {
            handler.postDelayed(closableRunnable, 5000)
        }
    }

    open fun onClosableState() {}

    abstract fun resetWidgets()

    override fun onDismiss(dialog: DialogInterface?) {
        handler.removeCallbacks(closableRunnable)
        resetWidgets()
    }

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}