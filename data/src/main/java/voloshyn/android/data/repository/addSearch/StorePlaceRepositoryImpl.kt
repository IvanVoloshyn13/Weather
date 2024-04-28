package voloshyn.android.data.repository.addSearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.domain.model.addSearchPlace.SearchPlace
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository
import javax.inject.Inject

class StorePlaceRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StorePlaceRepository {
    override suspend fun store(place: SearchPlace) = withContext(ioDispatcher) {
       database.getPlaceDao().storeNewPlace(place.toPlaceEntity())
    }
}

fun SearchPlace.toPlaceEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        latitude = latitude, longitude = longitude, timezone = timezone, country = country,
        countryCode = countryCode
    )
}

