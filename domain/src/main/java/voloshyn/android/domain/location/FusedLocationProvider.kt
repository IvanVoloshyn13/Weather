package voloshyn.android.domain.location


import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.LocationProviderError
import voloshyn.android.domain.model.Place


interface FusedLocationProvider {

    suspend fun getCurrentUserLocation(): AppResult<Place, LocationProviderError>

}