package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.model.addSearchPlace.Place


interface SavePlaceRepository {
    suspend fun savePlace(place: Place): Long
}