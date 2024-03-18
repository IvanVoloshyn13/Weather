package voloshyn.android.network.retrofit.apiServices

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import voloshyn.android.network.retrofit.models.weather.WeatherResponse

interface ApiWeatherService {

    @GET(WEATHER_ENDPOINT)
    suspend fun fetchWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<WeatherResponse>

    @GET(WEATHER_ENDPOINT)
    suspend fun fetchWeatherDataForMultipleLocation(
        @Query("latitude") latitude: Array<Double>,
        @Query("longitude") longitude: Array<Double>
    ): Response<List<WeatherResponse>>

    companion object {
        const val WEATHER_ENDPOINT = "v1/forecast?&hourly=temperature_2m," +
                "weathercode&daily=weathercode" + ",temperature_2m_max,temperature_2m_min," +
                "sunrise,sunset,uv_index_max&timezone=auto"
    }
}