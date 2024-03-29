package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.addSearchPlace.ListSizeState
import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.MainWeatherInfo
import voloshyn.android.weather.gpsReceiver.GpsStatus

data class WeatherState(
    val location: String = "",
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: List<HourlyForecast> = ArrayList(),
    val dailyForecast: List<DailyForecast> = ArrayList(),
    val places: Pair<Int, List<Place>> = Pair(0, ArrayList()),
    val placesState: ListSizeState = ListSizeState.DEFAULT,
    val imageUrl: String = "",
    val gpsStatus: GpsStatus? = null,
    val networkStatus: NetworkStatus? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val weatherWidgetVisibility: Double = 0.0,
)