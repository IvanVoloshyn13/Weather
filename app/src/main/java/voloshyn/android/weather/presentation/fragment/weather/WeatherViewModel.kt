package voloshyn.android.weather.presentation.fragment.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.Resource
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val state = _weatherState.asStateFlow()


    fun getCurrentLocation() {
        viewModelScope.launch {
            val location = getCurrentLocationUseCase.invoke()
            when (location) {
                is Resource.Success -> {
                    location.data.let { data ->
                        _weatherState.update { state ->
                            state.copy(location = data.city, isLoading = false, isError = false)
                        }
                    }
                }

                is Resource.Error -> {
                    _weatherState.update { state ->
                        state.copy(
                            isError = true,
                            errorMessage = location.message
                        )
                    }
                }
            }
        }
    }

}