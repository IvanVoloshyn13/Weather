package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.error.AppError
import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.model.Place


interface GetSavedPlacesRepository {
    /**
     * Return saved list of [Place] from local dataBase
     * */
    suspend fun getSavedPlaces(): Flow<List<Place>>
}