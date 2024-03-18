package voloshyn.android.data.repository.weather.pager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.storage.database.AppDatabase
import voloshyn.android.domain.Resource
import voloshyn.android.domain.repository.weather.pager.SavedPlacesLocationRepository
import voloshyn.android.domain.useCase.weather.pager.LatitudeLongitude
import javax.inject.Inject

class SavedPlacesLocationRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SavedPlacesLocationRepository {

    override suspend fun getLatitudeLongitudeList(): Resource<LatitudeLongitude> =
        withContext(ioDispatcher) {
            return@withContext try {
                val latitude = db.getCityDao().getLatitude().toDoubleArray()
                val longitude = db.getCityDao().getLongitude().toDoubleArray()
                Resource.Success(data = LatitudeLongitude(latitude, longitude))
            } catch (e: Exception) {
                Resource.Error(e = e)
            }
        }

}