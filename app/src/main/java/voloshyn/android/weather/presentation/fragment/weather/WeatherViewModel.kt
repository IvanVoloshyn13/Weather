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
import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.CustomError
import voloshyn.android.domain.model.CurrentUserLocation
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.useCase.weather.FetchUnsplashImageByCityNameUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherForCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetPlaceByIdUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForLocationUseCase
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetPlaceById
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetSavedPlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.GetWeatherByCurrentLocation
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
        getDataByCurrentUserLocation()
    }


    fun onIntent(intent: WeatherScreenIntent) {
        when (intent) {
            GetWeatherByCurrentLocation -> {
                viewModelScope.launch {
                    getDataByCurrentUserLocation()
                }
            }

            is GetPlaceById -> {
                viewModelScope.launch { getPlaceById(intent.id) }

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

    private fun getDataByCurrentUserLocation() {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                )
            }
            val location = getCurrentLocation()
            if (location != null) {
                Log.d("WEATHER", "ViewModel")
                val weatherComponents = async {
                    getWeatherByLocation(
                        location.latitude,
                        location.longitude,
                        _weatherState.value.networkStatus ?: NetworkStatus.LOST
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
                        placeName = location.city,
                        weatherComponents = weatherComponents,
                        cityImage = imageUrl
                    )
                }
            }
        }
    }

    private suspend fun getPlaceById(id: Int) {
        val cityResource = getPlaceById.invoke(id)
        when (cityResource) {
            is Resource.Success -> {
                getDataBySearchedPlace(
                    cityResource.data
                )
            }

            is Resource.Error -> {}
        }
    }

    private fun getDataBySearchedPlace(place: Place) {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    isLoading = true,
                    location = place.name
                )
            }
            val weatherComponents = async {
                getWeatherByLocation(
                    place.latitude,
                    place.longitude,
                    _weatherState.value.networkStatus ?: NetworkStatus.LOST
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
            val imageUrl = async { getCityImage(place.name) }.await()
            if (weatherComponents != null) {
                updateUiState(
                    placeName = place.name,
                    weatherComponents = weatherComponents,
                    cityImage = imageUrl
                )
            }
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
        viewModelScope.launch {
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

    }


    private suspend fun getCurrentLocation(): CurrentUserLocation? {
        val result = getCurrentLocationUseCase.invoke()
        return try {
            result.toResult()
        } catch (e: Exception) {
            _weatherState.update {
                it.copy(
                    location = CurrentUserLocation.DEFAULT.city,
                )
            }
            null
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
        return try {
            weatherResource.toResult()
        } catch (e: CustomError) {
            null
        }
    }

    private suspend fun getCityImage(cityName: String): String {
        val cityResource = unsplashImageByCityNameUseCase.invoke(cityName)
        return try {
            cityResource.toResult().cityImageUrl
        } catch (e: Exception) {
            e.message ?: ""
        }

    }

    private fun getTimeForLocation(timeZoneId: String) {
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
        getDataByCurrentUserLocation()
    }

    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }

    private fun <T> Resource<T>.toResult(): T {
        return when (this) {
            is Resource.Success -> data
            is Resource.Error -> {
                _weatherState.update {
                    it.copy(
                        isLoading = false,
                        isError = e !is CustomError,
                        errorMessage = e.message ?: ""
                    )
                }
                viewModelScope.launch {
                    _sideEffectsState.emit(
                        SideEffects(
                            showErrorMessage = true,
                            errorMessage = e.message ?: ""
                        )
                    )
                }

                throw e
            }
        }
    }

}

