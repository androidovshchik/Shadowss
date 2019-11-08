package domain.shadowss.screen

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
import domain.shadowss.local.Preferences
import org.jetbrains.anko.locationManager
import timber.log.Timber

private typealias Controller = BaseController<out BaseView>

interface BaseView

fun BaseActivity<out Controller>.checkLocation() {
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
                    it.startResolutionForResult(
                        this,
                        BaseActivity.REQUEST_LOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e(e)
                }
            } else {
                Timber.e(it)
            }
        }
}

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity<C : Controller> : Activity(), BaseView {

    protected open val requiredLocation = false

    protected lateinit var controller: C

    protected lateinit var preferences: Preferences

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = BaseController<BaseView>(applicationContext) as C
        controller.bind(this)
        preferences = Preferences(applicationContext)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if (requiredLocation) {
                checkLocation()
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

        const val REQUEST_LOCATION = 100
    }
}