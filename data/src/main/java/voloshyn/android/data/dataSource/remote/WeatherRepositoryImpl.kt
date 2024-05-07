package voloshyn.android.data.dataSource.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toWeatherComponents
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.WeatherRepository
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.utils.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WeatherRepository {

    override suspend fun fetchWeather(
        latitude: Double,
        longitude: Double
    ): WeatherComponents = withContext(dispatcher) {
        try {
            val networkResult = executeApiCall({
                apiWeatherService.fetchWeatherData(
                    latitude = latitude,
                    longitude = longitude
                )
            }
            )
            return@withContext networkResult.toWeatherComponents()
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }


}




