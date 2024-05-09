package voloshyn.android.data.repository

import android.content.Context
import android.database.SQLException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.logError
import voloshyn.android.data.mappers.toDomainError
import voloshyn.android.data.mappers.toPlace
import voloshyn.android.data.mappers.toPlaceEntity
import voloshyn.android.data.mappers.toPlaceEntityArray
import voloshyn.android.data.mappers.toResultError
import voloshyn.android.data.mappers.toSearchedCityList
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.Places
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.utils.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiSearchCityService
import java.io.IOException
import java.util.logging.Logger
import javax.inject.Inject

private const val INITIAL_CITIES_LIST_SIZE = 4

class PlaceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchCityService: ApiSearchCityService,
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : PlaceRepository {

    override suspend fun searchByName(name: String): AppResult<Places, DataError.Network> =
        withContext(ioDispatcher) {
            try {
                val result = executeApiCall(call = {
                    searchCityService.searchCityByName(name = name)
                })
                return@withContext AppResult.Success(data = result.toSearchedCityList())

            } catch (e: ApiException) {
                return@withContext AppResult.Error(error = e.toResultError())
            } catch (e: Exception) {
                return@withContext AppResult.Error(error = DataError.Network.UNKNOWN_ERROR)
            }
        }

    override suspend fun storePlace(place: Place) {
        database.placeDao().storePlace(place.toPlaceEntity())
    }

    override suspend fun storePlaces(places: Array<Place>) {
        database.placeDao().storePlaces(places.toPlaceEntityArray())
    }

    override fun getPlaces(placesState: PlacesSizeState): Flow<Places> {
        val placesCount = database.placeDao().placesRowCount()
        var limit = 0
        return try {
            limit = findLimit(placesCount, placesState)
            val placeEntityList = database.placeDao().getAllPlaces(limit)
            val placesFlow: Flow<List<Place>> = placeEntityList.map {
                it.map { placeEntity ->
                    placeEntity.toPlace()
                }
            }
            placesFlow
        } catch (e: IOException) {
            logger.logError(this::class, e)
            throw e
        }
    }

    private fun findLimit(placesCount: Int, placesState: PlacesSizeState): Int {
        return when (placesState) {
            PlacesSizeState.DEFAULT -> {
                if (placesCount >= INITIAL_CITIES_LIST_SIZE) INITIAL_CITIES_LIST_SIZE else placesCount
            }

            PlacesSizeState.TRIM -> {
                INITIAL_CITIES_LIST_SIZE
            }

            PlacesSizeState.FULL -> {
                placesCount
            }
        }

    }


    override suspend fun getPlaceById(placeId: Int): AppResult<Place, DataError> {
        return try {
            val appResult = database.placeDao().getPlace(placeId)
            AppResult.Success(data = appResult.toPlace())
        } catch (e: SQLException) {
            AppResult.Error(error = e.toDomainError())
        }
    }
}

