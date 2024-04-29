package voloshyn.android.weather.presentation.fragment.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.NetworkStatus
import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.LocationProviderError
import voloshyn.android.domain.model.ListSizeState
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.useCase.weather.FetchWeatherAndImageDataUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForSelectedPlaceUseCase
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlace
import voloshyn.android.weather.presentation.fragment.weather.mvi.SideEffects
import voloshyn.android.weather.presentation.fragment.weather.mvi.TogglePlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherScreenIntent
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState
import javax.inject.Inject

const val CURRENT_LOCATION_DEFAULT_ID = 0

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val currentTime: GetTimeForSelectedPlaceUseCase,
    private val getSavedPlaces: GetSavedPlacesUseCase,
    private val weatherAndImage: FetchWeatherAndImageDataUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", "$throwable,  ${throwable.message}")
        _state.update {
            it.copy(
                isLoading = false
            )
        }
        return@CoroutineExceptionHandler
    }

    private var locationTimeJob: Job? = null
    private val viewModelScope = CoroutineScope(SupervisorJob() + exceptionHandler)
    private val _state =
        MutableStateFlow<WeatherState>(
            WeatherState()
        )
    val state = _state.asStateFlow()

    private val _sideEffectsState = MutableSharedFlow<SideEffects>()
    val sideEffects = _sideEffectsState.asSharedFlow()

    private val _time = MutableStateFlow<String>("")
    val time = _time.asStateFlow()

    init {
        viewModelScope.launch {
            /** #1 Main function to get weather data */
            fetchWeatherData(Place())
        }
    }

    /**
     *This is the function which is trigger all the rest function with necessary data for weatherState
     * */
    private suspend fun fetchWeatherData(_place: Place) {
        _state.emit(WeatherState(isLoading = true))
        val place = when (_place.id) {
            CURRENT_LOCATION_DEFAULT_ID -> {
                getCurrentUserLocation()
            }

            else -> {
                _place
            }
        }
        when (place) {
            Place.EMPTY_PLACE_ERROR -> TODO()
            else -> {
                getWeatherAndImage(
                    place
                )
                observeTime(_state.value.timeZone)
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onIntent(intent: WeatherScreenIntent) {
        when (intent) {
            FetchWeatherForCurrentLocation -> {
                Log.d("Model", "Intent")
                viewModelScope.launch {
                    Log.d("Model", "Intent1")
                    fetchWeatherData(Place())
                }
            }

            is FetchWeatherForSavedPlace -> {
                viewModelScope.launch { fetchWeatherData(intent.place) }
            }

            is UpdateNetworkStatus -> {
                updateNetworkStatus(intent.networkStatus)
            }

            is UpdateGpsStatus -> {
                updateGpsStatus(intent.gpsStatus)
            }

            is TogglePlaces -> {
                viewModelScope.launch {
                    when (_state.value.placesState) {
                        ListSizeState.FULL -> {
                            getSavedPlaces(ListSizeState.TRIM)
                            _state.update {
                                it.copy(placesState = ListSizeState.TRIM)
                            }
                        }

                        else -> {
                            getSavedPlaces(ListSizeState.FULL)
                            _state.update {
                                it.copy(placesState = ListSizeState.FULL)
                            }
                        }
                    }
                }

            }
        }
    }


    private suspend fun observeTime(timeZone: String) {
        if (locationTimeJob != null) {
            stopTimeObserve()
            if (locationTimeJob?.isCompleted == true) {
                updateTime(timeZone = timeZone)
            }
        } else {
            updateTime(timeZone = timeZone)
        }
    }


    /** When app is started it is default location for weather data. So if locationProvider will be disabled or
     * user denied permission we cant fetch weather data fo current location but we still can
     * search for favourite places and fetch weather data for them*/
    private suspend fun getCurrentUserLocation(): Place {
        val result = getCurrentLocationUseCase.invoke()
        return when (result) {
            is AppResult.Error -> {
                when (result.error) {
                    LocationProviderError.NO_LOCATION -> {
                        _state.update {
                            it.copy(
                                placeName = Place.EMPTY_PLACE_ERROR.name,
                                locationProviderError = Pair(true, TODO("Create text resources"))
                            )
                        }
                        Place.EMPTY_PLACE_ERROR
                    }

                    LocationProviderError.PROVIDER_ERROR -> {
                        _state.update {
                            it.copy(
                                placeName = Place.EMPTY_PLACE_ERROR.name,
                                locationProviderError = Pair(true, TODO("Create text resources"))
                            )
                        }
                        Place.EMPTY_PLACE_ERROR
                    }

                    LocationProviderError.NO_PERMISSION -> {
                        _state.update {
                            it.copy(
                                placeName = Place.EMPTY_PLACE_ERROR.name,
                                locationProviderError = Pair(true, TODO("Create text resources"))
                            )
                        }
                        Place.EMPTY_PLACE_ERROR
                    }
                }
            }

            is AppResult.Success -> {
                _state.update {
                    it.copy(
                        placeName = result.data.name,
                        locationProviderError = Pair(false, WeatherState.NO_ERROR)
                    )
                }
                with(result.data) {
                    Place(
                        name = name,
                        latitude = latitude,
                        longitude = longitude,
                        id = id,
                        timezone = timezone,
                        country = country,
                        countryCode = countryCode

                    )
                }

            }
        }
    }

    /** This function fetch weather for both current place from FusedLocationProvider
     * or saved place  */
    private suspend fun getWeatherAndImage(
        place: Place
    ) {
        val result = weatherAndImage.invoke(
            place
        )
        when (result) {
            is AppResult.Error -> {}
            is AppResult.Success -> {
                val dailyForecast = result.data.weatherComponents.dailyForecast
                val hourlyForecast = result.data.weatherComponents.hourlyForecast
                val mainWeatherInfo = result.data.weatherComponents.currentForecast
                _state.update {
                    it.copy(
                        currentForecast = mainWeatherInfo,
                        hourlyForecast = hourlyForecast,
                        dailyForecast = dailyForecast,
                        timeZone = result.data.weatherComponents.timezone ?: ""
                    )
                }
            }
        }
    }


    private suspend fun processNoNetwork() {
        _sideEffectsState.emit(
            SideEffects(
                showErrorMessage = true,
                errorMessage = "No network connection"
            )
        )
    }


    private suspend fun updateTime(timeZone: String) {
        locationTimeJob = viewModelScope.launch {
            currentTime.invoke(timeZone, true).cancellable().collectLatest { time ->
                _time.emit(time)
            }
        }
    }

    private suspend fun getSavedPlaces(listSizeState: ListSizeState) {
        val result = getSavedPlaces.invoke(listSizeState)

        result.collectLatest { list ->
            _state.update {
                it.copy(
                    places = Pair(list.size, list)
                )
            }
        }

    }


    fun updateWeatherWidgetVisibility(value: Double) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    weatherWidgetVisibility = value
                )
            }
        }
    }

    private fun updateGpsStatus(gps: GpsStatus) {
        _state.update { state ->
            state.copy(gpsStatus = gps)
        }
        //  if (gps == GpsStatus.AVAILABLE && _weatherState.value.location.isEmpty()) onChange()
    }

    private fun updateNetworkStatus(network: NetworkStatus) {
        _state.update { state ->
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

}



