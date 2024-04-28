package voloshyn.android.domain.location


import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.LocationProviderError
import voloshyn.android.domain.model.Place


interface FusedLocationProvider {

    suspend fun getCurrentUserLocation(): AppResult<Place, LocationProviderError>

}