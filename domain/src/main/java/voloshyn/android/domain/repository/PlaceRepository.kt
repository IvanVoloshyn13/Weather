package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.place.PlacesSizeState

typealias Places = List<Place>

interface PlaceRepository {
    /** Search place using open_meteo geoCoding service
     *  @param [name] */
    suspend fun searchByName(name: String): AppResult<Places, DataError.Network>

    /** Save place in local database table
     *  @param [place] */
    suspend fun storePlace(place: Place)

    /** Save place in local database table
     *  @param [places] */
    suspend fun storePlaces(places: Array<Place>)

    /** Return saved list of [Place] from local dataBase
     * @param [placesState]*/
    fun getPlaces(placesState: PlacesSizeState): Flow<Pair<Int, Places>>

    /** Return one [Place] from local dataBase
     * @param [placeId] */
    suspend fun getPlaceById(placeId: Int): AppResult<Place, DataError>
}