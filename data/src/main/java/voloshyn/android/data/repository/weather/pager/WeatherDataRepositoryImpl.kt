package voloshyn.android.data.repository.weather.pager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.data.mappers.toWeatherComponents
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.pager.WeatherDataRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WeatherDataRepository {

    override suspend fun fetchWeather(
        latitude: DoubleArray,
        longitude: DoubleArray,

    ): Resource<List<WeatherComponents>> = withContext(dispatcher) {
        try {
            val networkResult = executeApiCall({
                apiWeatherService.fetchWeatherDataForMultipleLocation(
                    latitude = latitude.toTypedArray(),
                    longitude = longitude.toTypedArray()
                )
            })
            when (networkResult) {
                is ApiResult.Success -> {
                    val weatherList = networkResult.data.map {
                        it.toWeatherComponents()
                    }
                    return@withContext Resource.Success(weatherList)
                }

                is ApiResult.Error -> {
                    return@withContext Resource.Error(e = networkResult.e)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ApiException -> return@withContext e.toResourceError(context)
                else -> throw Exception()
            }
        }
    }

}