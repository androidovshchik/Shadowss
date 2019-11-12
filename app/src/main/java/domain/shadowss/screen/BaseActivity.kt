package domain.shadowss.screen

import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import android.view.MenuItem
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import defpackage.marsh.*
import domain.shadowss.controller.Controller
import domain.shadowss.manager.WebSocketCallback
import org.jetbrains.anko.locationManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import timber.log.Timber

interface BaseView : WebSocketCallback

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : Activity(), KodeinAware, BaseView {

    override val kodein by closestKodein()

    protected open val requireLocation = false

    protected abstract val controller: Controller<out BaseView>

    override fun onStart() {
        super.onStart()
        controller.start()
    }

    override fun onSAPI(instance: SAPI) {}

    override fun onSAPO(instance: SAPO) {}

    override fun onSARM(instance: SARM) {}

    override fun onSARR(instance: SARR) {}

    override fun onSARV(instance: SARV) {}

    override fun onSCNG(instance: SCNG) {}

    override fun onSMNG(instance: SMNG) {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if (requireLocation) {
                checkLocation()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (resultCode != RESULT_OK) {
                    checkLocation()
                }
            }
        }
    }

    private fun checkLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return
        }
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(LocationRequest.create().apply {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    })
                    .setAlwaysShow(true)
                    .build()
            )
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(this, REQUEST_LOCATION)
                    } catch (e: Throwable) {
                        Timber.e(e)
                    }
                } else {
                    Timber.e(it)
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

    companion object {

        const val REQUEST_LOCATION = 100
    }
}