package domain.shadowss.manager

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import domain.shadowss.screen.BaseActivity
import timber.log.Timber

class LocationManager(context: Context) : Manager {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {
            Timber.d("onLocationAvailability $availability")
            reference.get()?.onLocationAvailability(availability.isLocationAvailable)
        }

        override fun onLocationResult(result: LocationResult?) {
            result?.lastLocation?.let {
                Timber.i("Last location is $it")
                reference.get()?.onLocationResult(it)
            } ?: run {
                Timber.w("Last location is null")
            }
        }
    }

    override fun init(vararg args: Any?) {
    }

    fun requestUpdates() {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun removeUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    fun checkLocation(activity: Activity) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return
        }
        LocationServices.getSettingsClient(activity)
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true)
                    .build()
            )
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(this, BaseActivity.REQUEST_LOCATION)
                    } catch (e: Throwable) {
                        Timber.e(e)
                    }
                } else {
                    Timber.e(it)
                }
            }
    }

    override fun release(vararg args: Any?) {}

    companion object {

        val locationRequest: LocationRequest
            get() = LocationRequest.create().apply {
                interval = 5_000L
                fastestInterval = 5_000L
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
    }
}