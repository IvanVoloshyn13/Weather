package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.location.FusedLocationProvider

class GetCurrentLocationUseCase(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke() = fusedLocationProvider.getCurrentUserLocation()
}