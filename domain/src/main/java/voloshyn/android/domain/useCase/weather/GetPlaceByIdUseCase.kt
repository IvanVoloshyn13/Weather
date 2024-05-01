package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.weather.SavedPlacesRepository

class GetPlaceByIdUseCase(private val repository: SavedPlacesRepository) {
    suspend fun invoke(placeId: Int): AppResult<Place, DataError> {
        return repository.getPlaceById(placeId)
    }
}