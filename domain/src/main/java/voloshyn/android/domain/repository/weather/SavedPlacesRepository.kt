package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place


interface SavedPlacesRepository {
    /**
     * Return saved list of [Place] from local dataBase
     * */
     fun getPlaces(): Flow<List<Place>>

     suspend fun getPlaceById(placeId:Int):AppResult<Place,DataError>
}