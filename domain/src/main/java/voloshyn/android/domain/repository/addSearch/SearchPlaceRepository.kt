package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.domain.model.Place

typealias PlacesList = List<Place>

interface SearchPlaceRepository {
    suspend fun searchPlaceByName(name: String): AppResult<PlacesList, DataError.Network>
}