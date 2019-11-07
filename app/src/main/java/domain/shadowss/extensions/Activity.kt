@file:Suppress("unused")

package domain.shadowss.extensions

import android.app.Activity

fun Activity.requestPermissions(requestCode: Int, vararg permissions: String) {
    if (isMarshmallowPlus()) {
        requestPermissions(permissions, requestCode)
    }
}