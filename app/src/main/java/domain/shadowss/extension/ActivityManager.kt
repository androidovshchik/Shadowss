@file:Suppress("unused", "DEPRECATION")

package domain.shadowss.extension

import android.app.ActivityManager
import android.app.Service

inline fun <reified T : Service> ActivityManager.isRunning(): Boolean {
    for (service in getRunningServices(Integer.MAX_VALUE)) {
        if (T::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}

fun ActivityManager.getTopActivity(packageName: String): String? {
    for (task in getRunningTasks(Integer.MAX_VALUE)) {
        task.topActivity?.let {
            if (it.packageName == packageName) {
                return it.className
            }
        }
    }
    return null
}