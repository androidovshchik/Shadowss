@file:Suppress("DEPRECATION")

package domain.shadowss.screen.fragments

import android.app.Fragment
import android.os.Bundle

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment : Fragment() {

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