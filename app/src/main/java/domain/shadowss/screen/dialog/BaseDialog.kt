@file:Suppress("DEPRECATION", "unused")

package domain.shadowss.screen.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Window
import domain.shadowss.extension.activity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import javax.annotation.OverridingMethodsMustInvokeSuper

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseDialog(activity: Activity) : Dialog(activity), KodeinAware,
    DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    override val kodein by closestKodein(activity)

    protected open val canBeClosed = true

    protected var isClosable = false

    private val handler = Handler()

    private val closableRunnable = Runnable {
        setCancelable(true)
        isClosable = true
        onClosable()
    }

    init {
        setCancelable(false)
        setOnShowListener(this)
        setOnDismissListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    @OverridingMethodsMustInvokeSuper
    override fun onShow(dialog: DialogInterface?) {
        if (canBeClosed) {
            handler.postDelayed(closableRunnable, 5000)
        }
    }

    open fun onClosable() {}

    @OverridingMethodsMustInvokeSuper
    override fun onDismiss(dialog: DialogInterface?) {
        handler.removeCallbacks(closableRunnable)
    }

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}