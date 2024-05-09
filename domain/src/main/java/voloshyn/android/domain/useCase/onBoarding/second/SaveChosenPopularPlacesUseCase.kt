package voloshyn.android.domain.useCase.onBoarding.second

import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.repository.PlaceRepository

class SaveChosenPopularPlacesUseCase(private val repository: PlaceRepository) {
    suspend fun invoke(places: Array<Place>) {
        if (places.isNotEmpty()) {
            repository.storePlaces(places)
        }
    }
}