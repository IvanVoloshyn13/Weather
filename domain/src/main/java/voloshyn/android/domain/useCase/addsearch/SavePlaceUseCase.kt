package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.repository.PlaceRepository

class SavePlaceUseCase(private val repository: PlaceRepository) {
    suspend fun invoke(place: Place) {
        return repository.storePlace(place)
    }
}