package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.NetworkStatus
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.place.PlacesSizeState
import voloshyn.android.domain.model.weather.components.CurrentForecast
import voloshyn.android.domain.model.weather.components.DailyForecast
import voloshyn.android.domain.model.weather.components.HourlyForecast
import voloshyn.android.weather.gpsReceiver.GpsStatus

data class WeatherState(
    val placeName: String = "",
    val timeZone: String = "",
    val currentForecast: CurrentForecast = CurrentForecast(),
    val hourlyForecast: List<HourlyForecast> = ArrayList(),
    val dailyForecast: List<DailyForecast> = ArrayList(),
    val places: Pair<Int, List<Place>> = Pair(0, ArrayList()),
    val placesState: PlacesSizeState = PlacesSizeState.DEFAULT,
    val imageUrl: String = "",
    val gpsStatus: GpsStatus? = null,
    val currentLocationIsActive: Boolean = true,
    val networkStatus: NetworkStatus? = null,
    val isLoading: Boolean = true,
    val unsplashNetworkError: Pair<Boolean, String> = Pair(false, NO_ERROR_MESSAGE),
    val weatherNetworkError: Pair<Boolean, String> = Pair(false, NO_ERROR_MESSAGE),
    val locationProviderError: Pair<Boolean, String> = Pair(false, NO_ERROR_MESSAGE),
    val localStorageError: Pair<Boolean, String> = Pair(false, NO_ERROR_MESSAGE),
    val unknownError: Pair<Boolean, String> = Pair(false, NO_ERROR_MESSAGE),
    val errorMessage: String = NO_ERROR_MESSAGE,

    ) {

    val isError =
        unsplashNetworkError.first
                && weatherNetworkError.first
                && locationProviderError.first
                && localStorageError.first
                && unknownError.first

    val showGpsDisabledDialog = when (gpsStatus) {
        GpsStatus.AVAILABLE -> false
        GpsStatus.UNAVAILABLE -> currentLocationIsActive
        null -> false
    }


    companion object {
        const val NO_ERROR_MESSAGE = ""
    }
}