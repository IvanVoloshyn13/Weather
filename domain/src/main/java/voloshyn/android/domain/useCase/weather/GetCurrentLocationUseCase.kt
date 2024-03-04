package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.model.CurrentUserLocation
import voloshyn.android.domain.model.Location
import voloshyn.android.domain.useCase.toResult

class GetCurrentLocationUseCase(private val fusedLocationProvider: FusedLocationProvider) {
    suspend fun invoke(): Location {
        val resource = fusedLocationProvider.getCurrentUserLocation()
        val currentLocation = resource.toResult()
        val location = Location(
            name = currentLocation.city,
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude
        )
        return location
    }
}