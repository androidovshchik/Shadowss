package domain.shadowss.screen.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import domain.shadowss.controller.BaseController
import org.jetbrains.anko.locationManager
import timber.log.Timber

interface BaseView

@SuppressLint("Registered")
open class BaseActivity<T : BaseController<out BaseView>> : Activity(), BaseView {

    protected open val requiredLocation = false

    protected lateinit var controller: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = BaseController<BaseView>(applicationContext) as T
        controller.bind(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if (requiredLocation) {
                checkLocation()
            }
        }
    }

    private fun checkLocation() {
        val isGpsAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsAvailable) {
            LocationServices.getSettingsClient(this)
                .checkLocationSettings(locationSettingsRequest)
                .addOnFailureListener {
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(this, REQUEST_LOCATION)
                        } catch (e: IntentSender.SendIntentException) {
                            Timber.e(e)
                        }
                    } else {
                        Timber.e(it)
                    }
                }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        controller.unbind()
        super.onDestroy()
    }

    companion object {

        private const val REQUEST_LOCATION = 100

        private val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            })
            /**
             * Whether or not location is required by the calling app in order to continue.
             * Set this to true if location is required to continue and false if having location provides better results,
             * but is not required. This changes the wording/appearance of the dialog accordingly.
             */
            .setAlwaysShow(true)
            .build()
    }
}