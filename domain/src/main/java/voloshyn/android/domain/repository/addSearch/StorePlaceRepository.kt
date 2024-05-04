package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.model.Place


interface StorePlaceRepository {
    suspend fun store(place: Place)
}