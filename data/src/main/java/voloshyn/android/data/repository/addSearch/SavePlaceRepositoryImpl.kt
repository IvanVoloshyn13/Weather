package voloshyn.android.data.repository.addSearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.storage.database.AppDatabase
import voloshyn.android.data.storage.database.PlaceEntity
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.addSearch.SavePlaceRepository
import javax.inject.Inject

class SavePlaceRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SavePlaceRepository {
    override suspend fun savePlace(place: Place): Long = withContext(ioDispatcher) {
        return@withContext database.getCityDao().saveNewPlace(place.toPlaceEntity())
    }
}

fun Place.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country,
        countryCode = countryCode
    )
}

fun PlaceEntity.toPlace(): Place {
    return Place(
        id = id,
        name = name,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country,
        countryCode = countryCode
    )
}
