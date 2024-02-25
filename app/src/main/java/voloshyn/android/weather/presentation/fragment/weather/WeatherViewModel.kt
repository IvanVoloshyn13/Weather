package voloshyn.android.weather.presentation.fragment.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetLocationById
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetSavedLocationsList
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetWeatherByCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.ShowLessCities
import voloshyn.android.weather.presentation.fragment.weather.mvi.ShowMoreCities
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherScreenIntent
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val weatherForCurrentLocation: FetchWeatherForCurrentLocationUseCase,
    private val currentTime: GetTimeForLocationUseCase,
    private val unsplashImageByCityNameUseCase: FetchUnsplashImageByCityNameUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
        _weatherState.update {
            it.copy(
                isLoading = false,
                isError = true
            )
        }
    }
    private var locationTimeJob: Job? = null

    private val viewModelScope = CoroutineScope(exceptionHandler)

    private val _weatherState =
        MutableStateFlow<WeatherState>(
            WeatherState()
        )
    val state = _weatherState.asStateFlow()

    init {
        getDataByCurrentUserLocation()
    }


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
                val weatherComponents = async {
                    getWeatherByLocation(
                        location.latitude,
                        location.longitude,
                        NetworkStatus.AVAILABLE
                    )
                }.await()

                if (locationTimeJob != null) {
                    stopTimeObserve()
                    if (locationTimeJob?.isCompleted == true) {
                        weatherComponents?.let {
                            getTimeForLocation(timeZoneId = it.timezone ?: "")
                        }
                    }
                } else {
                    weatherComponents?.let {
                        getTimeForLocation(timeZoneId = it.timezone ?: "")
                    }
                }
                val imageUrl = async { getCityImage(location.city) }.await()
                if (weatherComponents != null) {
                    updateUiState(
                        location = location,
                        weatherComponents = weatherComponents,
                        cityImage = imageUrl
                    )
                }
            }

        }
    }

    private fun updateUiState(
        location: CurrentUserLocation,
        weatherComponents: WeatherComponents,
        cityImage: String
    ) {
        val dailyForecast = weatherComponents.dailyForecast
        val hourlyForecast = weatherComponents.hourlyForecast
        val mainWeatherInfo = weatherComponents.mainWeatherInfo
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    location = location.city,
                    mainWeatherInfo = mainWeatherInfo,
                    hourlyForecast = hourlyForecast,
                    dailyForecast = dailyForecast,
                    backgroundImage = cityImage,
                    isLoading = false,
                    isError = false
                )
            }
        }

    }


    private suspend fun getCurrentLocation(): CurrentUserLocation? {
        val result = getCurrentLocationUseCase.invoke()
        return when (result) {
            is Resource.Success -> {
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
    ): WeatherComponents? {
        val weatherResource = weatherForCurrentLocation.invoke(
            latitude = latitude,
            longitude = longitude,
            networkStatus = networkStatus
        )
        return when (weatherResource) {
            is Resource.Success -> {
                weatherResource.data
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
                null
            }
        }
    }

    private suspend fun getCityImage(cityName: String): String {
        val cityResource = unsplashImageByCityNameUseCase.invoke(cityName)
        return when (cityResource) {
            is Resource.Success -> {
                    cityResource.data.cityImageUrl
            }

            is Resource.Error -> {
                _weatherState.update {
                    it.copy(
                        isError = true
                    )
                }
                ""
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
        _weatherState.update { state ->
            state.copy(gpsStatus = gps)
        }
//        onChange()
    }

    private fun updateNetworkStatus(network: NetworkStatus) {
        _weatherState.update { state ->
            state.copy(
                networkStatus = network,
            )
        }
        // onChange()

    }

    private fun onChange() {
        if (_weatherState.value.hourlyForecast?.isEmpty() == true &&
            _weatherState.value.networkStatus == NetworkStatus.AVAILABLE &&
            _weatherState.value.gpsStatus == GpsStatus.AVAILABLE
        ) {
            getDataByCurrentUserLocation()
        }
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }


}