package voloshyn.android.data.dataSource.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.nameLater.WeatherComponentsCreator
import voloshyn.android.domain.model.weather.components.WeatherComponents
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import voloshyn.android.network.http.utils.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiWeatherService: ApiWeatherService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WeatherRepository {

    @Inject
    lateinit var creator: WeatherComponentsCreator

    override suspend fun fetchWeather(
        latitude: Double,
        longitude: Double
    ): WeatherComponents = withContext(dispatcher) {
        wrapNetworkCallWithException {
            val networkResult = executeApiCall({
                apiWeatherService.fetchWeatherData(
                    latitude = latitude,
                    longitude = longitude
                )
            }
            )
            return@wrapNetworkCallWithException creator.toWeatherComponents(networkResult)

        }

    }


}

suspend fun <T> wrapNetworkCallWithException(call: suspend () -> T): T {
    return try {
        call()
    } catch (e: NoConnectivityException) {
        throw e
    } catch (e: ApiException) {
        throw e
    } catch (e: Exception) {
        throw e
    }
}







