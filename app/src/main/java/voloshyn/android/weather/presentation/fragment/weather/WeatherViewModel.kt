package voloshyn.android.weather.presentation.fragment.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.CurrentUserLocation
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.useCase.weather.FetchUnsplashImageByCityNameUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherForCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForLocationUseCase
import voloshyn.android.weather.gpsReceiver.GpsStatus
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val weatherForCurrentLocation: FetchWeatherForCurrentLocationUseCase,
    private val currentTime: GetTimeForLocationUseCase,
    private val unsplashImageByCityNameUseCase: FetchUnsplashImageByCityNameUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }
    private var locationTimeJob: Job? = null

    private val viewModelScope = CoroutineScope(exceptionHandler)

    private val _weatherState =
        MutableStateFlow<WeatherState>(
            WeatherState()
        )
    val state = _weatherState.asStateFlow()



    fun onIntent(intent: WeatherScreenIntent) {
        when (intent) {
            GetWeatherByCurrentLocation -> {
                viewModelScope.launch {
                    getDataByCurrentUserLocation()
                }
            }

            is GetLocationById -> {
            }

            is GetSavedLocationsList -> {
            }

            is UpdateNetworkStatus -> {
                updateNetworkStatus(intent.networkStatus)
            }

            is UpdateGpsStatus -> {
                updateGpsStatus(intent.gpsStatus)
            }

            is ShowMoreCities -> {
            }

            is ShowLessCities -> {

            }
        }
    }

    private fun getDataByCurrentUserLocation() {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(isLoading = true)
            }
            val location = getCurrentLocation()
            if (location != null) {
                val timezoneId =
                    getWeatherByLocation(
                        location.latitude,
                        location.longitude,
                        NetworkStatus.AVAILABLE
                    ) ?: ""

                if (locationTimeJob != null) {
                    stopTimeObserve()
                    if (locationTimeJob?.isCompleted == true) {
                        getTimeForLocation(timeZoneId = timezoneId)
                    }
                } else {
                    getTimeForLocation(timeZoneId = timezoneId)
                }
                getCityImage(location.city)
            }
            _weatherState.update {
                it.copy(isLoading = false)
            }
        }
    }


    private suspend fun getCurrentLocation(): CurrentUserLocation? {
        val result = getCurrentLocationUseCase.invoke()
        return when (result) {
            is Resource.Success -> {
                result.data.let { currentUserLocation ->
                    _weatherState.update {
                        it.copy(
                            location = currentUserLocation.city,

                            )
                    }
                }
                result.data
            }

            is Resource.Error -> {
                result.message?.let { message ->
                    _weatherState.update {
                        it.copy(
                            location = CurrentUserLocation.DEFAULT.city
                        )
                    }
                }
                null
            }
        }
    }

    private suspend fun getWeatherByLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): String? {
        val weatherResource = weatherForCurrentLocation.invoke(
            latitude = latitude,
            longitude = longitude,
            networkStatus = networkStatus
        )
        return when (weatherResource) {
            is Resource.Success -> {
                weatherResource.data.let { weatherData ->
                    val dailyForecast = weatherData.dailyForecast
                    val hourlyForecast = weatherData.hourlyForecast
                    val mainWeatherInfo = weatherData.mainWeatherInfo
                    _weatherState.update {
                        it.copy(
                            mainWeatherInfo = mainWeatherInfo,
                            dailyForecast = dailyForecast,
                            hourlyForecast = hourlyForecast,

                            )
                    }
                }
                weatherResource.data.timezone
            }

            is Resource.Error -> {
                weatherResource.message?.let { message ->
                    _weatherState.update {
                        it.copy(
                            mainWeatherInfo = WeatherComponents().mainWeatherInfo,
                            dailyForecast = WeatherComponents().dailyForecast,
                            hourlyForecast = WeatherComponents().hourlyForecast,

                            )
                    }
                }
                ""
            }
        }
    }

    private suspend fun getCityImage(cityName: String) {

        val cityResource = unsplashImageByCityNameUseCase.invoke(cityName)
        when (cityResource) {
            is Resource.Success -> {
                if (cityResource.data.cityImageUrl.isNotEmpty()) {
                    _weatherState.update {
                        it.copy(
                            backgroundImage = cityResource.data.cityImageUrl
                        )
                    }
                }
            }

            is Resource.Error -> {
                _weatherState.update {
                    it.copy(
                        isError = true
                    )
                }
            }
        }
    }

    private fun getTimeForLocation(timeZoneId: String) {
        locationTimeJob = viewModelScope.launch {
            currentTime.invoke(timeZoneId, true).cancellable().collectLatest { time ->
                _weatherState.update {
                    it.copy(
                        currentTime = time ?: ""
                    )
                }
            }
        }
    }

    private fun updateGpsStatus(gps: GpsStatus) {
        viewModelScope.launch {
            if(_weatherState.value.gpsStatus!=gps){
                getDataByCurrentUserLocation()
            }
            _weatherState.update { state ->
                state.copy(gpsStatus = gps)
            }
        }
    }

    private fun updateNetworkStatus(network: NetworkStatus) {
        viewModelScope.launch {
            _weatherState.update { state ->
                state.copy(networkStatus = network)
            }
        }
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }


}