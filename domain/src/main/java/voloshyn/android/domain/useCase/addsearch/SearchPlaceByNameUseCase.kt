package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.domain.repository.addSearch.PlacesList
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository

class SearchPlaceByNameUseCase(private val searchCityRepository: SearchPlaceRepository) {
    suspend fun invoke(locationName: String):AppResult< PlacesList,DataError.Network> {
       return searchCityRepository.searchPlaceByName(locationName)
    }
}