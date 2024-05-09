package voloshyn.android.weather.presentation.fragment.onBoarding.second

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
import voloshyn.android.data.dataSource.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.data.dataSource.popularPlacesStorage.PopularPlaceData
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceHandler
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceState
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.useCase.onBoarding.second.OnBoardingCompletedUseCase
import voloshyn.android.domain.useCase.onBoarding.second.SaveChosenPopularPlacesUseCase
import voloshyn.android.weather.di.PlacesMultiChoice
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val inMemoryPopularPlacesRepositoryImpl: InMemoryPopularPlacesRepositoryImpl,
    @PlacesMultiChoice private val multiChoiceHandlerImpl: MultiChoiceHandler<PopularPlaceData>,
    private val saveChosenPopularPlacesUseCase: SaveChosenPopularPlacesUseCase,
    private val onFinished: OnBoardingCompletedUseCase
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.toString())
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }
    private var viewModelScope: CoroutineScope = CoroutineScope(coroutineExceptionHandler)

    private val _popularPlacesData: MutableStateFlow<List<PopularPlaceData>> =
        MutableStateFlow(ArrayList())
    val popularPlace = _popularPlacesData.asStateFlow()

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
                _popularPlacesData.emit(it)
            }
        }
    }

    private fun merge(
        list: List<PopularPlaceData>,
        multiChoiceState: MultiChoiceState<PopularPlaceData>
    ): List<PopularPlaceData> {
        return list.map { place ->
            PopularPlaceData(
                image = place.image,
                name = place.name,
                isChecked = multiChoiceState.isChecked(place),
                id = place.id,
                country = place.country,
                countryCode = place.countryCode,
                latitude = place.latitude,
                longitude = place.longitude,
                timezone = place.timezone
            )
        }
    }

    fun toggleSelection(place: PopularPlaceData) {
        multiChoiceHandlerImpl.toggle(place)
    }

    fun checkedItems(): Array<Place> {
        val multiChoice = multiChoiceHandlerImpl as MultiChoiceState<PopularPlaceData>
        val elements = multiChoice.checkedItem
        val placesNameArray =
            Array<Place>(elements.size) { Place() }
        elements.map { it }
            .forEachIndexed { index, name ->
                placesNameArray[index] =
                    inMemoryPopularPlacesRepositoryImpl.toDomainPopularPlace(name)
            }
        return placesNameArray
    }

    fun save(array: Array<Place>) {
        val onBoardingIsFinished = true
        viewModelScope.launch {
            onFinished.invoke(onBoardingIsFinished)
            saveChosenPopularPlacesUseCase.invoke(array)
        }
    }


}

