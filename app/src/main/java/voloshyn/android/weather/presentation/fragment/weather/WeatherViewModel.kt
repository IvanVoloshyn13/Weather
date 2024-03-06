package voloshyn.android.weather.presentation.fragment.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.CurrentUserLocation
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.useCase.weather.FetchUnsplashImageByCityNameUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherForCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetPlaceByIdUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForLocationUseCase
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlace
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetSavedPlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.ShowLessPlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.ShowMorePlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.SideEffects
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherScreenIntent
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState
import javax.inject.Inject

const val INITIAL_CITIES_LIST_SIZE = 4

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val weatherForCurrentLocation: FetchWeatherForCurrentLocationUseCase,
    private val currentTime: GetTimeForLocationUseCase,
    private val unsplashImageByCityNameUseCase: FetchUnsplashImageByCityNameUseCase,
    private val getPlaceById: GetPlaceByIdUseCase,
    private val getSavedPlaces: GetSavedPlacesUseCase
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
    private val _sideEffectsState = MutableSharedFlow<SideEffects>()
    val sideEffects = _sideEffectsState.asSharedFlow()

    init {
        viewModelScope.launch {
            fetchWeather(null)
        }
    }

    fun onIntent(intent: WeatherScreenIntent) {
        when (intent) {
            FetchWeatherForCurrentLocation -> {
                viewModelScope.launch {
                    fetchWeather(null)
                }
            }

            is FetchWeatherForSavedPlace -> {
                viewModelScope.launch { fetchWeather(intent.id) }
            }

            is GetSavedPlaces -> {
                TODO()
            }

            is UpdateNetworkStatus -> {
                updateNetworkStatus(intent.networkStatus)
            }

            is UpdateGpsStatus -> {
                updateGpsStatus(intent.gpsStatus)
            }

            is ShowMorePlaces -> {
                TODO()
            }

            is ShowLessPlaces -> {
                TODO()
            }
        }
    }

    private suspend fun fetchWeather(
        id: Int?
    ) {
        _weatherState.update {
            it.copy(
                isLoading = true,
                isError = false
            )
        }
        try {
            val location = if (id == null) getCurrentLocationUseCase.invoke()
                .toResult() else getPlaceById.invoke(id)

            loadData(
                location.name,
                location.latitude,
                location.longitude,
                _weatherState.value.networkStatus ?: NetworkStatus.LOST
            )
        } catch (e: Exception) {
            _weatherState.update {
                it.copy(
                    location = CurrentUserLocation.DEFAULT.city,
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: ""
                )
            }
            //TODO()
        }
    }

    private suspend fun loadData(
        name: String,
        latitude: Double,
        longitude: Double,
        network: NetworkStatus
    ) {
        viewModelScope.launch {
            val weatherComponents = async {
                getWeatherByLocation(
                    latitude,
                    longitude,
                    network
                )
            }.await()

            if (locationTimeJob != null) {
                stopTimeObserve()
                if (locationTimeJob?.isCompleted == true) {
                    weatherComponents?.let {
                        updateTime(timeZoneId = it.timezone ?: "")
                    }
                }
            } else {
                weatherComponents.let {
                    updateTime(timeZoneId = it.timezone ?: "")
                }
            }
            val imageUrl = async { fetchCityImage(name) }.await()
            updateUiState(
                placeName = name,
                weatherComponents = weatherComponents,
                cityImage = imageUrl
            )
        }
    }

    private fun updateUiState(
        placeName: String,
        weatherComponents: WeatherComponents,
        cityImage: String
    ) {
        val dailyForecast = weatherComponents.dailyForecast
        val hourlyForecast = weatherComponents.hourlyForecast
        val mainWeatherInfo = weatherComponents.mainWeatherInfo
        _weatherState.update {
            it.copy(
                location = placeName,
                mainWeatherInfo = mainWeatherInfo,
                hourlyForecast = hourlyForecast,
                dailyForecast = dailyForecast,
                backgroundImage = cityImage,
                isLoading = false,
                isError = false
            )
        }
    }

    private suspend fun getWeatherByLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): WeatherComponents {
        return weatherForCurrentLocation.invoke(
            latitude = latitude,
            longitude = longitude,
            networkStatus = networkStatus
        ).toResult()
    }

    private suspend fun fetchCityImage(cityName: String): String {
        val data = unsplashImageByCityNameUseCase.invoke(cityName).toResult()
        return data.cityImageUrl
    }

    private suspend fun updateTime(timeZoneId: String) {
        locationTimeJob = viewModelScope.launch {
            currentTime.invoke(timeZoneId, true).cancellable().collectLatest { time ->
                _weatherState.update {
                    it.copy(
                        currentTime = time
                    )
                }
            }
        }
    }

    fun updateWeatherWidgetVisibility(value: Double) {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    weatherWidgetVisibility = value
                )
            }
        }
    }

    private fun updateGpsStatus(gps: GpsStatus) {
        _weatherState.update { state ->
            state.copy(gpsStatus = gps)
        }
        //  if (gps == GpsStatus.AVAILABLE && _weatherState.value.location.isEmpty()) onChange()
    }

    private fun updateNetworkStatus(network: NetworkStatus) {
        _weatherState.update { state ->
            state.copy(
                networkStatus = network,
            )
        }
        // if (network == NetworkStatus.AVAILABLE && _weatherState.value.dailyForecast.isEmpty()) onChange()
    }

    private fun onChange() {
        // fetchWeatherForCurrentUserLocation()
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }

    private suspend fun emitError(message: String?) {
        _sideEffectsState.emit(
            SideEffects(
                showErrorMessage = true,
                errorMessage = message ?: "Cant load data"
            )
        )
    }

    private suspend fun <T> T.toResult(): T {
        return try {
            this
        } catch (e: Exception) {
            emitError(e.message)
            throw e
        }
    }

}



