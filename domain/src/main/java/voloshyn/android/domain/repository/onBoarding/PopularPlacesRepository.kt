package voloshyn.android.domain.repository.onBoarding

import voloshyn.android.domain.model.onBoarding.PopularPlace

interface PopularPlacesRepository {
    suspend fun savePlaces(places: Array<PopularPlace>):List<Long>
}