package voloshyn.android.weather.presentation.fragment.weather

import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.MainWeatherInfo
import voloshyn.android.weather.gpsReceiver.GpsStatus

data class WeatherState(
    val location: String = "Location",
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: List<HourlyForecast>? = ArrayList(),
    val dailyForecast: List<DailyForecast>? = ArrayList(),
    val currentTime: String = "00:00",
    val backgroundImage: String = "",
    val gpsStatus: GpsStatus? = null,
    val networkStatus: NetworkStatus? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = "",
)