package voloshyn.android.weather.fragment.weather

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
   private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

}