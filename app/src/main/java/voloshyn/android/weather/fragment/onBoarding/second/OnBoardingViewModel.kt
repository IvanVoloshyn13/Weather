package voloshyn.android.weather.fragment.onBoarding.second

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import voloshyn.android.data.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.data.popularPlacesStorage.PopularPlace
import voloshyn.android.data.popularPlacesStorage.multichoice.MultiChoiceHandler
import voloshyn.android.data.popularPlacesStorage.multichoice.MultiChoiceState
import voloshyn.android.weather.di.PlacesMultiChoice
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val inMemoryPopularPlacesRepositoryImpl: InMemoryPopularPlacesRepositoryImpl,
    @PlacesMultiChoice private val multiChoiceHandlerImpl: MultiChoiceHandler<PopularPlace>
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.toString())
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }
    private var viewModelScope: CoroutineScope = CoroutineScope(coroutineExceptionHandler)

    private val _popularPlaces: MutableStateFlow<List<PopularPlace>> = MutableStateFlow(ArrayList())
    val popularPlace = _popularPlaces.asStateFlow()

    init {
        viewModelScope.launch {
            multiChoiceHandlerImpl.setItemsFlow(
                viewModelScope,
                inMemoryPopularPlacesRepositoryImpl.getPopularPlaces()
            )
            val combinedFlow = combine(
                inMemoryPopularPlacesRepositoryImpl.getPopularPlaces(),
                multiChoiceHandlerImpl.listen(),
                ::merge
            )
            combinedFlow.collectLatest {
                _popularPlaces.emit(it)
            }
        }
    }

    private fun merge(
        list: List<PopularPlace>,
        multiChoiceState: MultiChoiceState<PopularPlace>
    ): List<PopularPlace> {
        return list.map { place ->
            PopularPlace(
                image = place.image,
                name = place.name,
                isChecked = multiChoiceState.isChecked(place)
            )
        }
    }

    fun toggleSelection(place: PopularPlace) {
        multiChoiceHandlerImpl.toggle(place)
    }

    fun checkedItems() {
        val multiChoice = multiChoiceHandlerImpl as MultiChoiceState<PopularPlace>
        val elements = multiChoice.checkedItem
        Log.d("TAG", elements.toString())
    }

}

