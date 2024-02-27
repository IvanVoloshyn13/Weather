package voloshyn.android.domain.useCase.addsearch

import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository

class SearchPlaceByNameUseCase(private val searchCityRepository: SearchPlaceRepository) {
    suspend fun invoke(locationName: String) = searchCityRepository.searchPlaceByName(locationName)
}