package domain.shadowss.manager

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.locationManager
import timber.log.Timber

@Suppress("MemberVisibilityCanBePrivate")
class LocationManager(context: Context) {

    val observer = PublishSubject.create<Location>().toSerialized()

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationManager = context.locationManager

    private val locationRequest = LocationRequest.create().apply {
        interval = 5_000L
        fastestInterval = 5_000L
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationAvailability(availability: LocationAvailability) {
            Timber.d("onLocationAvailability $availability")
        }

        override fun onLocationResult(result: LocationResult?) {
            result?.lastLocation?.let {
                observer.onNext(it)
            } ?: run {
                Timber.w("Last received location is null")
            }
        }
    }

    val isEnabled: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    fun requestUpdates() {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun checkLocation(activity: Activity) {
        if (isEnabled) {
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
                if (!activity.isFinishing && it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(activity, REQUEST_LOCATION)
                    } catch (e: Throwable) {
                        Timber.e(e)
                    }
                } else {
                    Timber.e(it)
                }
            }
    }

    fun removeUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    companion object {

        const val REQUEST_LOCATION = 100
    }
}