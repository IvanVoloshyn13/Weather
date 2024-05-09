package voloshyn.android.weather.presentation.fragment.weather

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import voloshyn.android.domain.NetworkStatus
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.mapToData
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.model.weather.WeatherAndImage
import voloshyn.android.domain.useCase.addsearch.SearchPlaceByNameUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherAndImageDataUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetPlaceByIdUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForSelectedPlaceUseCase
import voloshyn.android.weather.gpsReceiver.GpsStatus
import voloshyn.android.weather.presentation.fragment.BaseViewModel
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForCurrentLocation
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlace
import voloshyn.android.weather.presentation.fragment.weather.mvi.FetchWeatherForSavedPlaceById
import voloshyn.android.weather.presentation.fragment.weather.mvi.TogglePlaces
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateGpsStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.UpdateNetworkStatus
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherScreenIntent
import voloshyn.android.weather.presentation.fragment.weather.mvi.WeatherState
import voloshyn.android.weather.renderResult.toStringResources
import javax.inject.Inject

private const val CURRENT_LOCATION_DEFAULT_ID = 0
private const val EMPTY_STRING = ""

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val currentTime: GetTimeForSelectedPlaceUseCase,
    private val getSavedPlaces: GetSavedPlacesUseCase,
    private val getPlace: GetPlaceByIdUseCase,
    private val weatherAndImage: FetchWeatherAndImageDataUseCase,
    private val searchPlaceByNameUseCase: SearchPlaceByNameUseCase,
) : BaseViewModel() {


    private var locationTimeJob: Job? = null

    private val _state =
        MutableStateFlow<WeatherState>(
            WeatherState()
        )
    val state = _state.asStateFlow()

    val errorState = baseErrorState.asSharedFlow()

    private val _time = MutableStateFlow<String>("")
    val time = _time.asStateFlow()

    private val _blurState = MutableStateFlow<Double>(0.0)
    val blurState = _blurState.asStateFlow()

    init {
        getSavedPlaces(placesSizeState = PlacesSizeState.DEFAULT)
        viewModelScope.launch {
            /** #1 Main function to get weather data */
            fetchWeatherData(Place())
        }
    }


    fun onIntent(intent: WeatherScreenIntent) {
        when (intent) {
            FetchWeatherForCurrentLocation -> {
                viewModelScope.launch {
                    fetchWeatherData(Place())
                }
            }

            is FetchWeatherForSavedPlace -> {
                viewModelScope.launch {
                    fetchWeatherData(intent.place)
                }
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
                        PlacesSizeState.FULL -> {
                            getSavedPlaces(PlacesSizeState.TRIM)
                            _state.update {
                                it.copy(placesState = PlacesSizeState.TRIM)
                            }
                        }

                        PlacesSizeState.TRIM, PlacesSizeState.DEFAULT -> {
                            getSavedPlaces(PlacesSizeState.FULL)
                            _state.update {
                                it.copy(placesState = PlacesSizeState.FULL)
                            }
                        }
                    }
                }

            }

            is FetchWeatherForSavedPlaceById -> {
                viewModelScope.launch {
                    val place = getPlaceById(intent.placeID)
                    fetchWeatherData(place)
                }

            }
        }
    }

    /** It is ok */
    private fun getSavedPlaces(placesSizeState: PlacesSizeState) {
        viewModelScope.launch {
            val result = getSavedPlaces.invoke(placesSizeState)
            result.collectLatest { list ->
                _state.update {
                    it.copy(
                        places = Pair(list.size, list),
                    )
                }
            }
        }

    }

    /** It is ok */
    private suspend fun getPlaceById(placeId: Int): Place {
        val appResult = getPlace.invoke(placeId)
        return when (appResult) {
            is AppResult.Success -> {
                appResult.data
            }

            is AppResult.Error -> {
                emitErrorAndResetStatus(appResult.error.toStringResources())
                Place.EMPTY_PLACE_ERROR
            }

        }
    }

    /**
     *This is the main function which is trigger all the rest function with necessary data for weatherState
     * */
    private suspend fun fetchWeatherData(_place: Place) {
        _state.update {
            it.copy(isLoading = true)
        }
        val place = when (_place.id) {
            CURRENT_LOCATION_DEFAULT_ID -> {
                getCurrentUserLocationWithTimezone()
            }

            else -> {
                _place
            }
        }
        val weatherAndImage: WeatherAndImage = getWeatherAndImage(place)
        updateState(place.name, weatherAndImage)
        observeTime(place.timezone)


    }

    /** When app is started it is default location for weather data. So if locationProvider will be disabled or
     * user denied permission we cant fetch weather data fo current location but we still can
     * search for favourite places and fetch weather data for them*/
    private suspend fun getCurrentUserLocationWithTimezone(): Place {
        val result = getCurrentLocationUseCase.invoke()
        return when (result) {
            is AppResult.Success -> {
                val placeWithTimezone = placeWithTimezone(result.data)
                with(result.data) {
                    Place(
                        name = name,
                        latitude = latitude,
                        longitude = longitude,
                        id = id,
                        timezone = placeWithTimezone.timezone,
                        country = placeWithTimezone.country,
                        countryCode = placeWithTimezone.countryCode
                    )
                }
            }

            is AppResult.Error -> {
                result.error.toStringResources()
                Place.EMPTY_PLACE_ERROR
            }


        }
    }

    private suspend fun placeWithTimezone(place: Place): Place {
        if (place.name.isBlank()) return place
        return try {
            val appResult = searchPlaceByNameUseCase.invoke(place.name)
            when (appResult) {
                is AppResult.Success -> {
                    val placeWithTimezone =
                        appResult.data.firstOrNull()
                    placeWithTimezone ?: place
                }

                is AppResult.Error -> {
                    Place()
                }
            }

        } catch (e: Exception) {
            place
        }
    }


    /** This function fetch weather for both current place from FusedLocationProvider
     * or saved place  */
    private suspend fun getWeatherAndImage(
        place: Place
    ): WeatherAndImage {
        val result = weatherAndImage.invoke(
            place
        )
        return when (result) {
            is AppResult.Error -> {
                var weatherAndImage: WeatherAndImage = WeatherAndImage()
                result.mapToData(
                    dataBlock = {
                        weatherAndImage = it
                    },
                    errorBlock = {
                        emitErrorAndResetStatus(result.error.toStringResources())
                    }
                )
                weatherAndImage
            }

            is AppResult.Success -> {
                result.data
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

    private suspend fun updateTime(timeZone: String) {
        locationTimeJob = viewModelScope.launch {
            currentTime.invoke(timeZone, true).cancellable().collectLatest { time ->
                _time.emit(time)
            }
        }
    }


    fun updateWeatherWidgetVisibility(value: Double) {
        viewModelScope.launch {
            _blurState.emit(value)
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


    private suspend fun stopTimeObserve() {
        locationTimeJob?.cancelAndJoin()
    }

    private fun updateState(placeName: String, weatherAndImage: WeatherAndImage) {
        _state.update {
            it.copy(
                currentForecast = weatherAndImage.weatherComponents.currentForecast,
                hourlyForecast = weatherAndImage.weatherComponents.hourlyForecast,
                dailyForecast = weatherAndImage.weatherComponents.dailyForecast,
                timeZone = weatherAndImage.weatherComponents.timezone ?: "",
                imageUrl = weatherAndImage.image.url,
                isLoading = false,
                placeName = placeName

            )
        }
    }

}



