package voloshyn.android.weather.presentation.fragment.weather.pager

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import voloshyn.android.domain.useCase.weather.pager.FetchMultipleWeatherDataUseCase
import voloshyn.android.domain.useCase.weather.pager.GetSavedPlacesLocationUseCase
import voloshyn.android.domain.useCase.weather.pager.LatitudeLongitude
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @Inject constructor(
    private val savedLocations: GetSavedPlacesLocationUseCase,
    private val weather: FetchMultipleWeatherDataUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
    }
    private val viewModelScope = CoroutineScope(exceptionHandler)

    init {
        viewModelScope.launch {
            val result = savedLocations.invoke()
            val weather = weather.invoke(
                LatitudeLongitude(
                    result.latitudeArray,
                    result.longitudeArray
                )
            )
            weather.forEach {
                Log.d("Weather", it.mainWeatherInfo.minTemperature.toString())
            }

        }

    }
}