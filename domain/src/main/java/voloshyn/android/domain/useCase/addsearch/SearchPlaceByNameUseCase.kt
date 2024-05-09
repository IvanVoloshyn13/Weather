package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.Places

class SearchPlaceByNameUseCase(private val repository: PlaceRepository) {
    suspend fun invoke(locationName: String):AppResult< Places,DataError.Network> {
       return repository.searchByName(locationName)
    }
}