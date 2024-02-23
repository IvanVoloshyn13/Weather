package voloshyn.android.domain.location


import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.CurrentUserLocation


interface FusedLocationProvider {

    suspend fun getCurrentUserLocation(): Resource<CurrentUserLocation>

}