@file:Suppress("DEPRECATION")

package domain.shadowss.screen.dialog

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import domain.shadowss.extension.activity
import org.jetbrains.anko.inputMethodManager
import javax.annotation.OverridingMethodsMustInvokeSuper

open class BaseDialog(activity: Activity) : Dialog(activity) {

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}

@Suppress("MemberVisibilityCanBePrivate")
open class BaseFragment : Fragment() {

    protected val args: Bundle
        get() = arguments ?: Bundle()

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        activity?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}

open class BaseDialogFragment : DialogFragment() {

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