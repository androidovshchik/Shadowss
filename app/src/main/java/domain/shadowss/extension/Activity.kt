@file:Suppress("unused")

package domain.shadowss.extension

import android.app.Activity

fun Activity.requestPermissions(requestCode: Int, vararg permissions: String) {
    if (isMarshmallowPlus()) {
        requestPermissions(permissions, requestCode)
    }
}