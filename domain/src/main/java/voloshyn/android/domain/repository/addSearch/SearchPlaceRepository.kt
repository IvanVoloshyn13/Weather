package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.SearchPlace

typealias PlacesList = List<SearchPlace>

interface SearchPlaceRepository {
    suspend fun searchPlaceByName(name: String): Resource<PlacesList>
}