package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.repository.addSearch.PlacesList
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.domain.toResult

class SearchPlaceByNameUseCase(private val searchCityRepository: SearchPlaceRepository) {
    suspend fun invoke(locationName: String): PlacesList {
       return searchCityRepository.searchPlaceByName(locationName).toResult()
    }
}