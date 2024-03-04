package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.model.Location
import voloshyn.android.domain.repository.weather.GetPlaceByIdRepository
import voloshyn.android.domain.useCase.toResult

class GetPlaceByIdUseCase(private val repository: GetPlaceByIdRepository) {

    suspend fun invoke(id: Int): Location {
        val place = repository.getPlaceById(id).toResult()
        val location =
            Location(name = place.name, latitude = place.latitude, longitude = place.longitude)
        return location
    }
}