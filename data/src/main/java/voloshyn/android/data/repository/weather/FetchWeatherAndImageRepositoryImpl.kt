package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResultError
import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.repository.cache.WeatherAndImageCacheRepository
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.repository.weather.WeatherRepository
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import java.io.IOException
import java.net.UnknownHostException
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
            AppResult.Success(data = cache.get(place.id))
        } catch (e: IOException) {
            AppResult.Error(
                data = onNetworkException(place.id),
                error = DataError.Network.N0_CONNECTION
            )
        } catch (e: ApiException) {
            AppResult.Error(data = onNetworkException(place.id), error = e.toResultError())
        } catch (e: SQLException) {
            AppResult.Error(data = null, error = DataError.Locale.LOCAL_STORAGE_ERROR)
        } catch (e: Exception) {
            AppResult.Error(data = null, error = DataError.Network.UNKNOWN_ERROR)
        }

    }

    private suspend fun update(place: Place) = withContext(ioDispatcher) {
        val weatherComponentsDeferred =
            async { weatherRepository.fetchWeather(place.latitude, place.longitude) }
        val imageDeferred =
            async { unsplashImageRepository.fetchCurrentPlaceImageByName(place.name) }
        val weatherAndImage =
            WeatherAndImage(weatherComponentsDeferred.await(), imageDeferred.await())
        cache.store(placeId = place.id, weatherAndImage, place)
    }


    /** Get data from local database if network request fails
     * @throws [SQLException]
     * */
    private suspend fun onNetworkException(placeId: Int): WeatherAndImage {
        return cache.get(placeId = placeId)
    }


}