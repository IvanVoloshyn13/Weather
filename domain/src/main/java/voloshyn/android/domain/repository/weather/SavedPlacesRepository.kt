package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.PlacesSizeState


interface SavedPlacesRepository {
    /** Return saved list of [Place] from local dataBase */
    fun getPlaces(placesState:PlacesSizeState): Flow<List<Place>>

    /** Return one [Place] from local dataBase  */
    suspend fun getPlaceById(placeId: Int): AppResult<Place, DataError.Locale>
}