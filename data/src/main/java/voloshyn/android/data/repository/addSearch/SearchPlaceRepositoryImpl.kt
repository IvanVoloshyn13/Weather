package voloshyn.android.data.repository.addSearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.addSearch.PlacesList
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiSearchCityService
import voloshyn.android.network.retrofit.models.search.PlacesSearchResponse
import voloshyn.android.network.retrofit.models.search.SearchedPlaces
import javax.inject.Inject


class SearchPlaceRepositoryImpl @Inject constructor(
    private val searchCityService: ApiSearchCityService,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : SearchPlaceRepository {

    override suspend fun searchPlaceByName(name: String): Resource<PlacesList> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(call = {
                    searchCityService.searchCityByName(name = name)
                })
                return@withContext when (result) {
                    is ApiResult.Success -> {
                        Resource.Success(data = result.data.toSearchedCityList())
                    }

                    is ApiResult.Error -> {
                        Resource.Error(e = result.e)
                    }
                }
            } catch (e: ApiException) {
                return@withContext e.toResourceError()
            }
        }
}

fun PlacesSearchResponse.toSearchedCityList(): ArrayList<Place> {
    return this.citiesList.map { location: SearchedPlaces ->
        Place(
            id = location.id,
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude,
            timezone = location.timezone,
            country = location.country,
            countryCode = location.countyCode
        )
    } as ArrayList<Place>

}