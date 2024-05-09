package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.repository.PlaceRepository

class GetSavedPlacesUseCase(private val repository: PlaceRepository) {
     fun invoke(placesState: PlacesSizeState): Flow<List<Place>> {
        val result = repository.getPlaces(placesState)
       return  result

    }

}

