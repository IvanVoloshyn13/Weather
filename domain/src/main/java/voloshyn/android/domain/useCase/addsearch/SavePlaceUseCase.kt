package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.addSearch.SavePlaceRepository

class SavePlaceUseCase(private val saveRepository: SavePlaceRepository) {
    suspend fun invoke(place: Place): Long {
        return saveRepository.savePlace(place)
    }
}