package domain.shadowss.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.scottyab.rootbeer.RootBeer
import domain.shadowss.extension.areGranted
import domain.shadowss.extension.isMarshmallowPlus
import domain.shadowss.extension.isOreoPlus
import domain.shadowss.extension.requestPermissions
import org.jetbrains.anko.powerManager

@Suppress("SpellCheckingInspection")
class FuncManager : Manager {

    override fun init(vararg args: Any?) {}

    fun checkrights(context: Context): Boolean = context.run {
        var granted: Boolean
        if (isOreoPlus()) {
            granted = areGranted(Manifest.permission.ANSWER_PHONE_CALLS, *DANGER_PERMISSIONS)
            if (!granted) {
                if (this is Activity) {
                    requestPermissions(
                        REQUEST_PERMISSIONS,
                        Manifest.permission.ANSWER_PHONE_CALLS,
                        *DANGER_PERMISSIONS
                    )
                }
                return@run false
            }
        } else {
            granted = areGranted(*DANGER_PERMISSIONS)
            if (!granted) {
                if (this is Activity) {
                    requestPermissions(REQUEST_PERMISSIONS, *DANGER_PERMISSIONS)
                }
                return@run false
            }
        }
        if (isMarshmallowPlus()) {
            granted = powerManager.isIgnoringBatteryOptimizations(packageName)
            if (!granted) {
                if (this is Activity) {
                    startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                }
                return@run false
            }
        }
        return@run true
    }

    fun checkinternet() {

    }

    fun wssregconnect() {

    }

    fun checkuid() {

    }

    fun checkroot(context: Context) {
        val rootBeer = RootBeer(context)
        if (rootBeer.isRootedWithoutBusyBoxCheck) {

        }
    }

    fun checksim() {

    }

    fun checkmcc() {

    }

    fun checkregion() {

    }

    fun checkversion() {

    }

    fun checkmobile() {

    }

    override fun release(vararg args: Any?) {}

    companion object {

        const val REQUEST_PERMISSIONS = 200

        private val DANGER_PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
