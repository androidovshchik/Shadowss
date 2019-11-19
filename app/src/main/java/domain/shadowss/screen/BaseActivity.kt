package domain.shadowss.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import defpackage.watchLeaks
import domain.shadowss.controller.Controller
import domain.shadowss.controller.ControllerReference
import domain.shadowss.local.Preferences
import domain.shadowss.manager.LocationManager
import domain.shadowss.model.User
import org.jetbrains.anko.intentFor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

interface BaseView : ControllerReference

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : Activity(), KodeinAware, BaseView {

    private val parentKodein by closestKodein()

    override val kodein: Kodein by Kodein.lazy {

        extend(parentKodein)

        import(screenModule)
    }

    protected abstract val controller: Controller<out BaseView>

    protected val locationManager: LocationManager by instance()

    protected val preferences: Preferences by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences.apply {
            if (token != null) {
                when (user) {
                    User.MANAGER.id -> startActivity(intentFor<ManagerActivity>())
                    User.DRIVER.id -> startActivity(intentFor<DriverActivity>())
                }
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        controller.start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            when (this) {
                is ManagerActivity, is DriverActivity -> {
                    locationManager.checkLocation(this)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        controller.callback(this, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.callback(this, requestCode, resultCode)
        when (requestCode) {
            LocationManager.REQUEST_LOCATION -> {
                if (resultCode != RESULT_OK) {
                    locationManager.checkLocation(this)
                }
            }
        }
    }

    override fun onStop() {
        controller.stop()
        super.onStop()
    }

    protected inline fun onUiThread(crossinline action: () -> Unit) {
        runOnUiThread {
            if (!isFinishing) {
                action()
            }
        }
    }

    override fun onDestroy() {
        controller.release()
        watchLeaks(this)
        super.onDestroy()
    }
}