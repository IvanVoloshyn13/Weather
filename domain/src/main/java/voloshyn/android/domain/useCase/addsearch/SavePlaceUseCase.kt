package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.model.addSearchPlace.SearchPlace
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository

class SavePlaceUseCase(private val saveRepository: StorePlaceRepository) {
    suspend fun invoke(searchPlace: SearchPlace) {
        return saveRepository.store(searchPlace)
    }
}