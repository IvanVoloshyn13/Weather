package voloshyn.android.weather.presentation.fragment.addSearchPlaces

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.data.R
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.useCase.addsearch.SavePlaceUseCase
import voloshyn.android.domain.useCase.addsearch.SearchPlaceByNameUseCase
import voloshyn.android.weather.presentation.fragment.addSearchPlaces.mvi.FragmentSearchState
import voloshyn.android.weather.presentation.fragment.base.BaseViewModel
import voloshyn.android.weather.presentation.fragment.base.toStringResources
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlaceByNameUseCase: SearchPlaceByNameUseCase,
    private val savePlaceUseCase: SavePlaceUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<FragmentSearchState> =
        MutableStateFlow(FragmentSearchState())
    val state = _state.asStateFlow()

    val errorState = baseErrorState.asSharedFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _state.emit(
                FragmentSearchState(
                    isLoading = true
                )
            )
            if (query.length >= 3) {
                try {
                    val appResult = searchPlaceByNameUseCase.invoke(query)
                    when (appResult) {
                        is AppResult.Error -> {
                            emitErrorAndResetStatus(appResult.error.toStringResources())
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    places = emptyList()
                                )
                            }
                        }

                        is AppResult.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    places = appResult.data
                                )
                            }
                        }

                    }

                } catch (e: Exception) {
                    Log.d("CITY_ERROR", e.message.toString())
                    _state.emit(
                        FragmentSearchState(
                            isLoading = false,
                            places = emptyList()
                        )
                    )
                    emitErrorAndResetStatus(R.string.unknown_error)
                }
            } else{
                _state.emit(
                    FragmentSearchState(
                        isLoading = false
                    )
                )
            }
        }
    }

    fun savePlace(place: Place) {
        viewModelScope.launch {
            savePlaceUseCase.invoke(place)
        }
    }
}