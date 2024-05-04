package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place

typealias Places = List<Place>

interface SearchPlaceRepository {
    suspend fun searchPlaceByName(name: String): AppResult<Places, DataError.Network>
}