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

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseDialog(activity: Activity) : Dialog(activity), KodeinAware,
    DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    override val kodein by closestKodein(activity)

    protected open val canGoBack = true

    protected var allowClose = false

    private val handler = Handler()

    private val runnable = Runnable {
        setCancelable(canGoBack)
        allowClose = canGoBack
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

    override fun onShow(dialog: DialogInterface?) {
        if (canGoBack) {
            handler.postDelayed(runnable, 5000)
        }
    }

    open fun onClosable() {}

    override fun onDismiss(dialog: DialogInterface?) {

    }

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}