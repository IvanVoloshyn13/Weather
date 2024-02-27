package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.repository.addSearch.toPlace
import voloshyn.android.data.storage.database.AppDatabase
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.weather.GetPlaceByIdRepository
import java.io.IOException
import javax.inject.Inject

class GetPlaceByIdRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetPlaceByIdRepository {
    override suspend fun getPlaceById(id: Int): Resource<Place> = withContext(ioDispatcher) {
        val city = database.getCityDao().getPlaceById(id)
        return@withContext if (city != null) {
            Resource.Success(data = city.toPlace())
        } else {
            Resource.Error(e = IOException())
        }
    }
}