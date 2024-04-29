package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.NetworkStatus
import voloshyn.android.domain.model.ListSizeState
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.addSearchPlace.SearchPlace
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.CurrentForecast
import voloshyn.android.weather.gpsReceiver.GpsStatus

data class WeatherState(
    val placeName: String = "",
    val timeZone: String = "",
    val currentForecast: CurrentForecast = CurrentForecast(),
    val hourlyForecast: List<HourlyForecast> = ArrayList(),
    val dailyForecast: List<DailyForecast> = ArrayList(),
    val places: Pair<Int, List<Place>> = Pair(0, ArrayList()),
    val placesState: ListSizeState = ListSizeState.DEFAULT,
    val imageUrl: String = "",
    val gpsStatus: GpsStatus? = null,
    val networkStatus: NetworkStatus? = null,
    val isLoading: Boolean = true,
    val unsplashNetworkError: Pair<Boolean, String> = Pair(false, NO_ERROR),
    val weatherNetworkError: Pair<Boolean, String> = Pair(false, NO_ERROR),
    val locationProviderError: Pair<Boolean, String> = Pair(false, NO_ERROR),
    val localStorageError: Pair<Boolean, String> = Pair(false, NO_ERROR),
    val unknownError: Pair<Boolean, String> = Pair(false, NO_ERROR),
    val errorMessage: String = NO_ERROR,
    val weatherWidgetVisibility: Double = 0.0,
) {
    val isError =
        unsplashNetworkError.first
                && weatherNetworkError.first
                && locationProviderError.first
                && localStorageError.first
                && unknownError.first

    companion object {
        const val NO_ERROR = ""
    }
}