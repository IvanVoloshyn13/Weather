package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.model.PlacesSizeState
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.weather.SavedPlacesRepository




class GetSavedPlacesUseCase(private val repository: SavedPlacesRepository) {
     fun invoke(placesState: PlacesSizeState): Flow<List<Place>> {
        val result = repository.getPlaces(placesState)
       return  result

    }

}

