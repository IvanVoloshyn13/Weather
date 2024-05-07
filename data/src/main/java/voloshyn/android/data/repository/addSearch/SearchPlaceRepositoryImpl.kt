package voloshyn.android.data.repository.addSearch

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.logError
import voloshyn.android.data.mappers.toResultError
import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.addSearch.Places
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.utils.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiSearchCityService
import voloshyn.android.network.retrofit.models.search.PlacesSearchResponse
import voloshyn.android.network.retrofit.models.search.SearchedPlaces
import java.io.IOException
import java.util.logging.Logger
import javax.inject.Inject


class SearchPlaceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchCityService: ApiSearchCityService,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val logger: Logger
) : SearchPlaceRepository {

    override suspend fun searchPlaceByName(name: String): AppResult<Places, DataError.Network> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(call = {
                    searchCityService.searchCityByName(name = name)
                })
                return@withContext AppResult.Success(data = result.toSearchedCityList())

            } catch (e: ApiException) {
                return@withContext AppResult.Error(error = e.toResultError())
            } catch (e:Exception){
                return@withContext AppResult.Error(error =DataError.Network.UNKNOWN_ERROR )
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