@file:Suppress("unused")

package domain.shadowss.extension

import android.widget.EditText

fun EditText.setTextSelection(text: CharSequence?) {
    (text ?: "").let {
        setText(it)
        setSelection(it.length)
    }
}