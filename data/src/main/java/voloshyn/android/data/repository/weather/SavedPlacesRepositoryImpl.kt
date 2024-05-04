package voloshyn.android.data.repository.weather


import android.database.SQLException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.PlacesSizeState
import voloshyn.android.domain.repository.weather.SavedPlacesRepository
import java.io.IOException
import javax.inject.Inject

const val INITIAL_CITIES_LIST_SIZE = 4

class SavedPlacesRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SavedPlacesRepository {
    override fun getPlaces(placesState: PlacesSizeState): Flow<List<Place>> {
        val placesCount = database.getPlaceDao().placesRowCount()
        var limit = 0
        return try {
            limit = findLimit(placesCount, placesState)
            val placeEntityList = database.getPlaceDao().getAllPlaces(limit)
            val placesFlow: Flow<List<Place>> = placeEntityList.map {
                it.map { placeEntity ->
                    placeEntity.toPlace()
                }
            }
            placesFlow
        } catch (e: IOException) {
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

    override suspend fun getPlaceById(placeId: Int): AppResult<Place, DataError.Locale> {
        return try {
            val appResult = database.getPlaceDao().getPlace(placeId)
            AppResult.Success(data = appResult.toPlace())
        } catch (e: SQLException) {
            AppResult.Error(error = DataError.Locale.LOCAL_STORAGE_ERROR)
        }

    }

    private fun PlaceEntity.toPlace(): Place {
        return Place(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            country = country,
            countryCode = countryCode
        )
    }

}

