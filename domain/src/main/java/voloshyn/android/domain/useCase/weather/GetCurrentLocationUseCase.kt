package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.LocationProviderError
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.model.place.Place

class GetCurrentLocationUseCase(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke(): AppResult<Place, LocationProviderError> {
        return fusedLocationProvider.getCurrentUserLocation()
    }
}