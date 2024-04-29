package voloshyn.android.domain.repository.addSearch

import voloshyn.android.domain.model.addSearchPlace.SearchPlace


interface StorePlaceRepository {
    suspend fun store(searchPlace: SearchPlace)
}