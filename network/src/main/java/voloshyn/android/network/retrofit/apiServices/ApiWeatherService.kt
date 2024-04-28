package voloshyn.android.network.retrofit.apiServices

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import voloshyn.android.network.retrofit.models.weather.WeatherResponse
import voloshyn.android.network.retrofit.utils.ApiParameters

interface ApiWeatherService {

    @GET(WEATHER_ENDPOINT)
    suspend fun fetchWeatherData(
        @Query(ApiParameters.LATITUDE) latitude: Double,
        @Query(ApiParameters.LONGITUDE) longitude: Double,
        @Query(ApiParameters.HOURLY) hourly: Array<String> = arrayOf(
            "weathercode",
            "temperature_2m"
        ),
        @Query(ApiParameters.DAILY) daily: Array<String> = arrayOf(
            "weathercode",
            "temperature_2m_max",
            "temperature_2m_min",
            "sunrise",
            "sunset",
            "uv_index_max"
        ),
        @Query(ApiParameters.TIMEZONE) timeZone: String = "auto",
    ): Response<WeatherResponse>

    companion object {
        const val WEATHER_ENDPOINT = "forecast?"
    }
}

