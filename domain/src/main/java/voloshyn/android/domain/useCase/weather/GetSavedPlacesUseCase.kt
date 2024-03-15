package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.model.addSearchPlace.ListSizeState
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import voloshyn.android.domain.useCase.toResult

const val INITIAL_CITIES_LIST_SIZE = 4

class GetSavedPlacesUseCase(private val repository: GetSavedPlacesRepository) {
    suspend fun invoke(listSizeState: ListSizeState): Pair<Int, List<Place>> {
        val list = repository.getSavedCityList().toResult()
        val listSize = list.size
        return when (listSizeState) {
            ListSizeState.FULL -> {
                Pair(listSize, list)
            }

            ListSizeState.TRIM -> {
                val trimList = list.dropLast(list.size - INITIAL_CITIES_LIST_SIZE)
                Pair(listSize, trimList)
            }

            ListSizeState.DEFAULT -> {
                if (list.size > INITIAL_CITIES_LIST_SIZE) {
                    val trimList = list.dropLast(list.size - INITIAL_CITIES_LIST_SIZE)
                    Pair(listSize, trimList)
                } else Pair(listSize, list)
            }
        }
    }

}

