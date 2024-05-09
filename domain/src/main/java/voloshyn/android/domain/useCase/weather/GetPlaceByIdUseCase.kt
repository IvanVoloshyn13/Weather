package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.repository.PlaceRepository

class GetPlaceByIdUseCase(private val repository: PlaceRepository) {
    suspend fun invoke(placeId: Int): AppResult<Place, DataError> {
        return repository.getPlaceById(placeId)
    }
}