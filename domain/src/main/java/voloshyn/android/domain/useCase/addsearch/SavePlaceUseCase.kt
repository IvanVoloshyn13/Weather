package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository

class SavePlaceUseCase(private val saveRepository: StorePlaceRepository) {
    suspend fun invoke(place: Place) {
        return saveRepository.store(place)
    }
}