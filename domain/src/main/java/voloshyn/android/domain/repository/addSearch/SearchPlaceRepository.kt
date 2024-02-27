package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place

typealias PlacesList = List<Place>

interface SearchPlaceRepository {
    suspend fun searchPlaceByName(name: String): Resource<PlacesList>
}