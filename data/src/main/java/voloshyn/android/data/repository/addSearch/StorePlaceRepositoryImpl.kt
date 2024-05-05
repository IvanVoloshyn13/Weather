package voloshyn.android.data.repository.addSearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toPlaceEntity
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository
import javax.inject.Inject

class StorePlaceRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StorePlaceRepository {
    override suspend fun store(place: Place) = withContext(ioDispatcher) {
       database.placeDao().storeNewPlace(place.toPlaceEntity())
    }
}



