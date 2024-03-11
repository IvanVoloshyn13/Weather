package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.repository.addSearch.toPlace
import voloshyn.android.data.storage.database.AppDatabase
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import java.io.IOException
import javax.inject.Inject

class GetSavedPlacesRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetSavedPlacesRepository {
    override suspend fun getSavedCityList(): Resource<List<Place>> = withContext(ioDispatcher) {
        return@withContext try {
            val cityListEntity = database.getCityDao().getAllPlaces()
            val searchedCities = cityListEntity.map {
                it.toPlace()
            }
            Resource.Success(data = searchedCities)
        } catch (e: IOException) {
            Resource.Error(e = e)
        }
    }
}