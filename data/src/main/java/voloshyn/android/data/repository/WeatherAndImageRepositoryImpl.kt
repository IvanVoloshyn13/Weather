package voloshyn.android.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepository
import voloshyn.android.data.dataSource.remote.UnsplashRepository
import voloshyn.android.data.dataSource.remote.WeatherRepository
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toDomainError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.weather.WeatherAndImage
import voloshyn.android.domain.repository.WeatherAndImageRepository
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import java.io.IOException
import java.sql.SQLException
import java.util.logging.Logger
import javax.inject.Inject

class WeatherAndImageRepositoryImpl @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    private val weatherRepository: WeatherRepository,
    private val localStorage: WeatherAndImageLocalDataSourceRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : WeatherAndImageRepository {


    override suspend fun get(place: Place): AppResult<WeatherAndImage, DataError> {
        return try {
            val weatherAndImage = fetchData(place)
            store(weatherAndImage, place)
            AppResult.Success(data = localStorage.get(place.id))
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

    private suspend fun fetchData(place: Place): WeatherAndImage = withContext(ioDispatcher) {
        val weatherComponentsDeferred =
            async { weatherRepository.fetchWeather(place.latitude, place.longitude) }
        val imageDeferred =
            async {
                unsplashRepository.fetchImageByName(place.name)
            }
        return@withContext WeatherAndImage(weatherComponentsDeferred.await(), imageDeferred.await())

    }

    private suspend fun store(weatherAndImage: WeatherAndImage, place: Place) =
        withContext(ioDispatcher) {
            localStorage.store(placeId = place.id, weatherAndImage, place)
        }


    /** Get data from local database if network request fails
     * @throws [SQLException]
     * */
    private suspend fun onNetworkException(placeId: Int): WeatherAndImage {
        return localStorage.get(placeId = placeId)
    }


}