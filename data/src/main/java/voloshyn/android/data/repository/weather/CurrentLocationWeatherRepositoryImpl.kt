package voloshyn.android.data.repository.weather

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.R
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.data.mappers.toWeatherComponents
import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.NoNetworkError
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import javax.inject.Inject

class CurrentLocationWeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CurrentLocationWeatherRepository {

    override suspend fun fetchWeatherForLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Resource<WeatherComponents> = withContext(dispatcher) {
        if (networkStatus == NetworkStatus.AVAILABLE) {
            try {
                Log.d("WEATHER", "1")
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
                        return@withContext Resource.Error(e = networkResult.e)
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is ApiException -> return@withContext e.toResourceError(context)
                    else -> throw Exception()
                }
            }
        } else {
            return@withContext Resource.Error(e = NoNetworkError(context.getString(R.string.network_error)))
        }
    }

}