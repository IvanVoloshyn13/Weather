package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toDomainError
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
import java.sql.SQLException
import java.util.logging.Logger
import javax.inject.Inject

class FetchWeatherAndImageRepositoryImpl @Inject constructor(
    private val unsplashImageRepository: UnsplashImageRepository,
    private val weatherRepository: WeatherRepository,
    private val cache: WeatherAndImageCacheRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : FetchWeatherAndImageRepository {


    override suspend fun get(place: Place): AppResult<WeatherAndImage, DataError> {
        return try {
            val weatherAndImage = update(place)
            store(weatherAndImage, place)
            AppResult.Success(data = cache.get(place.id))
        } catch (e: ApiException) {
            val data = onNetworkException(place.id)
            AppResult.Error(
                data = data,
                error = e.toDomainError()
            )
        }catch (e:NoConnectivityException){
            val data = onNetworkException(place.id)
            AppResult.Error(
                data = data,
                error = e.toDomainError()
            )
        }
        catch (e: IOException) {
            val data = onNetworkException(place.id)
            AppResult.Error(
                data = data,
                error = e.toDomainError()
            )
        } catch (e: SQLException) {
            AppResult.Error(error = e.toDomainError())
        } catch (e: Exception) {
            AppResult.Error(error = e.toDomainError())
        }

    }

    private suspend fun update(place: Place): WeatherAndImage = withContext(ioDispatcher) {
        val weatherComponentsDeferred =
            async { weatherRepository.fetchWeather(place.latitude, place.longitude) }
        val imageDeferred =
            async {
                unsplashImageRepository.fetchCurrentPlaceImageByName(place.name)
            }
        return@withContext WeatherAndImage(weatherComponentsDeferred.await(), imageDeferred.await())

    }

    private suspend fun store(weatherAndImage: WeatherAndImage, place: Place) =
        withContext(ioDispatcher) {
            cache.store(placeId = place.id, weatherAndImage, place)
        }


    /** Get data from local database if network request fails
     * @throws [SQLException]
     * */
    private suspend fun onNetworkException(placeId: Int): WeatherAndImage {
        return cache.get(placeId = placeId)
    }


}