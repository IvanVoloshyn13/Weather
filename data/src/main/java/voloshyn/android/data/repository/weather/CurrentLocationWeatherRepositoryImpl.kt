package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.data.mappers.toWeatherComponents
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository
import voloshyn.android.http.exceptions.ApiException
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import javax.inject.Inject

class CurrentLocationWeatherRepositoryImpl @Inject constructor(
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CurrentLocationWeatherRepository {

    override suspend fun fetchWeatherForLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Resource<WeatherComponents> = withContext(dispatcher) {
        try {
            val networkResult = executeApiCall({
                apiWeatherService.fetchWeatherData(
                    latitude = latitude,
                    longitude = longitude
                )
            })
            when (networkResult) {
                is ApiResult.Success -> {
                    return@withContext Resource.Success(networkResult.data.toWeatherComponents())
                }

                is ApiResult.Error -> {
                    return@withContext Resource.Error(message = networkResult.message)
                }
            }
        } catch (e: ApiException) {
            return@withContext e.toResourceError()
        }
    }

}