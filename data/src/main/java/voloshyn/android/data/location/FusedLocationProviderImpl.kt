package voloshyn.android.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.LocationProviderError
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.model.place.CurrentUserLocation
import voloshyn.android.domain.model.place.Place
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FusedLocationProviderImpl @Inject constructor(
    @ApplicationContext val context: Context
) : FusedLocationProvider {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentUserLocation(
    ): AppResult<Place, LocationProviderError> {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasPermission = context.checkLocationPermission()
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!hasPermission) {
            return AppResult.Error(error = LocationProviderError.NO_PERMISSION)
        } else if (!isGpsEnabled && !isNetworkEnabled) {
            return AppResult.Error(
                error = LocationProviderError.PROVIDER_ERROR
            )
        }
        return suspendCancellableCoroutine { continuation ->
            var currentUserLocation: CurrentUserLocation
            val geocoder = Geocoder(context, Locale.getDefault())
            var latitude: Double
            var longitude: Double
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { locationNullable ->
                    locationNullable?.let { location ->
                        latitude = location.latitude
                        longitude = location.longitude
                        @Suppress("DEPRECATION")
                         val address = geocoder.getFromLocation(latitude, longitude, 1)
                        if (!address.isNullOrEmpty()) {
                            currentUserLocation = CurrentUserLocation(
                                latitude = latitude,
                                longitude = longitude,
                                city = address[0].locality
                            )


                            continuation.resume(AppResult.Success(data = currentUserLocation.toPlace())) {
                                continuation.resumeWithException(it)
                            }
                        }
                    } ?: continuation.resume(
                        AppResult.Error(error = LocationProviderError.NO_LOCATION)
                    )
                }
                    .addOnCanceledListener {
                        continuation.resume(
                            AppResult.Error(error = LocationProviderError.NO_LOCATION)
                        )
                    }
                    .addOnFailureListener {
                        continuation.resume(
                            AppResult.Error(error = LocationProviderError.NO_LOCATION)
                        )
                    }
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { locationNullable ->
                    locationNullable?.let { location ->
                        latitude = location.latitude
                        longitude = location.longitude
                        geocoder.getFromLocation(
                            latitude,
                            longitude,
                            1
                        ) {
                            if (it.size > 0) {
                                currentUserLocation = CurrentUserLocation(
                                    latitude = it[0].latitude,
                                    longitude = it[0].longitude,
                                    city = it[0].locality
                                )
                                continuation.resume(AppResult.Success(data = currentUserLocation.toPlace())) { exception ->
                                    continuation.resumeWithException(exception)
                                }
                            }
                        }
                    } ?: continuation.resume(
                        AppResult.Error(error = LocationProviderError.NO_LOCATION)
                    )
                }
                    .addOnCanceledListener {
                        continuation.resume(
                            AppResult.Error(error = LocationProviderError.NO_LOCATION)
                        )
                    }
                    .addOnFailureListener {
                        continuation.resume(
                            AppResult.Error(
                                error = LocationProviderError.NO_LOCATION
                            )
                        )
                    }
            }
        }

    }
}

fun Context.checkLocationPermission(): Boolean {
    val hasPermission = (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)
    return hasPermission
}

fun CurrentUserLocation.toPlace(): Place {
    return Place(
        name = this.city,
        latitude = this.latitude,
        longitude = this.longitude,
        id = 0,
        timezone = "",
        country = "",
        countryCode = ""

    )
}

