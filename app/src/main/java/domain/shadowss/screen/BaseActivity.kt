package domain.shadowss.screen

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import domain.shadowss.controller.Controller
import domain.shadowss.manager.LocationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

interface BaseView

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : Activity(), KodeinAware, BaseView {

    private val parentKodein by closestKodein()

    override val kodein: Kodein by Kodein.lazy {

        extend(parentKodein)

        import(screenModule)
    }

    protected open val requireLocation = false

    protected abstract val controller: Controller<out BaseView>

    protected val locationManager: LocationManager by instance()

    override fun onStart() {
        super.onStart()
        controller.start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if (requireLocation) {
                locationManager.checkLocation(this)
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
        controller.callback(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.callback(requestCode, resultCode)
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

    override fun onDestroy() {
        controller.release()
        super.onDestroy()
    }
}