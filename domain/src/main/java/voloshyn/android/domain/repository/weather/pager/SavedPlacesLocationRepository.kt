package voloshyn.android.domain.repository.weather.pager

import voloshyn.android.domain.Resource
import voloshyn.android.domain.useCase.weather.pager.LatitudeLongitude

interface SavedPlacesLocationRepository {
    suspend fun getLatitudeLongitudeList(): Resource<LatitudeLongitude>
}