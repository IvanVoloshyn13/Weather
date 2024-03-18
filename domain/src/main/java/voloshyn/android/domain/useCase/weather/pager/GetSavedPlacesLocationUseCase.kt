package voloshyn.android.domain.useCase.weather.pager

import voloshyn.android.domain.repository.weather.pager.SavedPlacesLocationRepository
import voloshyn.android.domain.useCase.toResult

class GetSavedPlacesLocationUseCase(private val repository: SavedPlacesLocationRepository) {
    suspend fun invoke(): LatitudeLongitude {
        val result = repository.getLatitudeLongitudeList().toResult()
        return result
    }
}

class LatitudeLongitude(val latitudeArray: DoubleArray, val longitudeArray: DoubleArray)