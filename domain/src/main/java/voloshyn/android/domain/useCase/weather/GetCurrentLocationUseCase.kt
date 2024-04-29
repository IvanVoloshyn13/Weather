package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.LocationProviderError
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.model.Place

class GetCurrentLocationUseCase(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke(): AppResult<Place, LocationProviderError> {
        return fusedLocationProvider.getCurrentUserLocation()
    }
}