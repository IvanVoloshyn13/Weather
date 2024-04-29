package voloshyn.android.data.repository.weather

import android.net.http.NetworkException
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.domain.error.AppError
import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.repository.cache.WeatherAndImageCacheRepository
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.repository.weather.WeatherRepository
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import java.sql.SQLException
import javax.inject.Inject

class FetchWeatherAndImageRepositoryImpl @Inject constructor(
    private val unsplashImageRepository: UnsplashImageRepository,
    private val weatherRepository: WeatherRepository,
    private val cache: WeatherAndImageCacheRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FetchWeatherAndImageRepository {


    override suspend fun get(place: Place): AppResult<WeatherAndImage, AppError> {
        return try {
            update(place)
            delay(3000)
           AppResult.Success(data = cache.get(place.id))
        } catch (e: NoConnectivityException) {
            onNetworkException()
            Log.d("Exc", "12")
            TODO()
        } catch (e: HttpException) {
            onNetworkException()
            Log.d("Exc", "13")
            TODO()
        } catch (e: SQLException) {
            Log.d("Exc", "14")
            TODO()
        } catch (e: Exception) {
            Log.d("Exc", e.message.toString())
            TODO()
        }

    }

    private suspend fun update(place: Place) = withContext(ioDispatcher) {
        val weatherComponentsDeferred =
            async { weatherRepository.fetchWeather(place.latitude, place.longitude) }
        val imageDeferred =
            async { unsplashImageRepository.fetchCurrentPlaceImageByName(place.name) }
        val weatherAndImage =
            WeatherAndImage(weatherComponentsDeferred.await(), imageDeferred.await())
        cache.store(placeId = place.id, weatherAndImage,place)
    }


    /** Get data from local database if network request fails
     * @throws [SQLException]
     * */
    private fun onNetworkException() {

    }


}