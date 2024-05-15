package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.Places

class GetSavedPlacesUseCase(private val repository: PlaceRepository) {
    fun invoke(placesState: PlacesSizeState): Flow<Pair<Int, Places>> {
        val result = repository.getPlaces(placesState)
        return result

    }

}

