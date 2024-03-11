package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.collectLatest
import voloshyn.android.domain.model.addSearchPlace.ListSizeState
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import voloshyn.android.domain.useCase.toResult

const val INITIAL_CITIES_LIST_SIZE = 4

class GetSavedPlacesUseCase(private val repository: GetSavedPlacesRepository) {
    suspend fun invoke(listSizeState: ListSizeState): List<Place> {
           val list= repository.getSavedCityList().toResult()
            return when (listSizeState) {
                ListSizeState.FULL -> {
                    list
                }

                ListSizeState.TRIM -> {
                    list.dropLast(list.size - INITIAL_CITIES_LIST_SIZE)
                }

                ListSizeState.DEFAULT -> {
                    if (list.size > INITIAL_CITIES_LIST_SIZE) {
                        list.dropLast(list.size - INITIAL_CITIES_LIST_SIZE)
                    } else list
                }
            }
        }

}

