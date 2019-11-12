@file:Suppress("DEPRECATION", "unused")

package domain.shadowss.screen.dialog

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import domain.shadowss.extension.activity
import org.jetbrains.anko.inputMethodManager
import javax.annotation.OverridingMethodsMustInvokeSuper

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseDialog(activity: Activity) : Dialog(activity) {

    protected open val canGoBack = true

    protected var allowClose = false

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (canGoBack) {
            Handler().postDelayed({
                setCancelable(true)
                allowClose = true
            }, 5000)
        }
    }

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class BaseDialogFragment : DialogFragment() {

    protected open val canGoBack = true

    protected var allowClose = false

    init {
        isCancelable = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (canGoBack) {
            Handler().postDelayed({
                isCancelable = true
                allowClose = true
            }, 5000)
        }
    }

    @OverridingMethodsMustInvokeSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return null
    }

    @OverridingMethodsMustInvokeSuper
    override fun dismiss() {
        view?.apply {
            context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
        super.dismiss()
    }

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        activity?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}