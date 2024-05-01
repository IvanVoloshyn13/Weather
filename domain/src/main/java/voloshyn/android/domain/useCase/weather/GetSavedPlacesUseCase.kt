package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.model.ListSizeState
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.repository.weather.SavedPlacesRepository


const val INITIAL_CITIES_LIST_SIZE = 4

class GetSavedPlacesUseCase(private val repository: SavedPlacesRepository) {
     fun invoke(listSizeState: ListSizeState): Flow<List<Place>> {
        val result = repository.getPlaces()
       return  result

    }

}

fun ToDoLater() {
//    val listSize = result.data.size
//    when (listSizeState) {
//        ListSizeState.FULL -> {
//            AppResult.Success(data = Pair(listSize, result.data))
//        }
//
//        ListSizeState.TRIM -> {
//            val trimList =
//                result.data.dropLast(result.data.size - INITIAL_CITIES_LIST_SIZE)
//            AppResult.Success(data = Pair(listSize, trimList))
//        }
//
//        ListSizeState.DEFAULT -> {
//            if (result.data.size > INITIAL_CITIES_LIST_SIZE) {
//                val trimList =
//                    result.data.dropLast(result.data.size - INITIAL_CITIES_LIST_SIZE)
//                AppResult.Success(data = Pair(listSize, trimList))
//            } else AppResult.Success(Pair(listSize, result.data))
//        }
//    }
}

