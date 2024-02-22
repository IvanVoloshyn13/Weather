package voloshyn.android.network.retrofit.apiServices

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import voloshyn.android.network.retrofit.models.searchcities.CitiesSearchResponse

interface ApiSearchCityService {
    @GET("v1/search?")
    suspend fun searchCityByName(
        @Query("name") cityName: String,
        @Query("count") listSize: Int = CITY_SEARCH_LIST_SIZE,
        @Query("language") language: String = LANGUAGE,
        @Query("format") format: String = FORMAT,
    ): Response<CitiesSearchResponse>

    companion object {
        const val CITY_SEARCH_LIST_SIZE = 20
        const val LANGUAGE = "en"
        const val FORMAT = "json"
    }
}