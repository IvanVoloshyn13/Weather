package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.repository.addSearch.Places
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository

class SearchPlaceByNameUseCase(private val searchCityRepository: SearchPlaceRepository) {
    suspend fun invoke(locationName: String):AppResult< Places,DataError.Network> {
       return searchCityRepository.searchPlaceByName(locationName)
    }
}