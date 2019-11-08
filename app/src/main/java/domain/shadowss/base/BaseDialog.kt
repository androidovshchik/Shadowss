package domain.shadowss.base

import android.app.Activity
import android.app.Dialog
import domain.shadowss.extension.activity

open class BaseDialog(activity: Activity) : Dialog(activity) {

    inline fun <reified T> makeCallback(action: T.() -> Unit) {
        context.activity()?.let {
            if (it is T && !it.isFinishing) {
                action(it)
            }
        }
    }
}