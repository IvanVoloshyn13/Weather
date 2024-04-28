package voloshyn.android.weather.presentation.fragment.addSearchPlaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.addSearchPlace.SearchPlace
import voloshyn.android.domain.useCase.addsearch.SavePlaceUseCase
import voloshyn.android.domain.useCase.addsearch.SearchPlaceByNameUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlaceByNameUseCase: SearchPlaceByNameUseCase,
    private val savePlaceUseCase: SavePlaceUseCase
) : ViewModel() {

    private val _places: MutableStateFlow<List<SearchPlace>> =
        MutableStateFlow(ArrayList<SearchPlace>())
    val places = _places.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun search(query: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            if (query.length >= 3) {
                try {
                    val list = searchPlaceByNameUseCase.invoke(query)
                    _places.emit(list)
                    _isLoading.emit(false)
                }catch (e:Exception){
                    Log.d("CITY_ERROR", e.message.toString())
                    _places.emit(ArrayList())
                    _isLoading.emit(false)
                }
            }
        }
    }

    fun saveCity(city: SearchPlace) {
        viewModelScope.launch {
            _isLoading.emit(true)
            savePlaceUseCase.invoke(city)
            _isLoading.emit(false)
        }
    }
}