package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place

interface GetPlaceByIdRepository {
    suspend fun getPlaceById(id: Int): Resource<Place>
}