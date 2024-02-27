package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place

interface GetSavedPlacesRepository {
    suspend fun getSavedCityList(): Resource<List<Place>>
}