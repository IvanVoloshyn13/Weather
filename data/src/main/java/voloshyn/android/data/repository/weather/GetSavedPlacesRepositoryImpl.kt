package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.domain.error.AppError
import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import java.io.IOException
import javax.inject.Inject

class GetSavedPlacesRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetSavedPlacesRepository {
    override suspend fun getSavedPlaces(): Flow<List<Place>> = flow {
        try {
                val placeEntityList = database.getPlaceDao().getAllPlaces()
                val placesFlow = placeEntityList.map { it ->
                    it.map {placeEntity->
                       placeEntity.toPlace()
                   }
                }
            placesFlow.collectLatest {
                emit(it)
            }
            } catch (e: IOException) {

            }
        }.flowOn(ioDispatcher)

    private fun PlaceEntity.toPlace(): Place {
        return Place(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            country = country,
            countryCode=countryCode
        )
    }
}

