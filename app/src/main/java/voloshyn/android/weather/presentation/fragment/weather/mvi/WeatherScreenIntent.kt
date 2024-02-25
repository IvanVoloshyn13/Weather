package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.weather.gpsReceiver.GpsStatus


sealed class WeatherScreenIntent
object GetWeatherByCurrentLocation : WeatherScreenIntent()
object GetSavedLocationsList : WeatherScreenIntent()
class GetLocationById(val cityId: Int) : WeatherScreenIntent()
object ShowMoreCities : WeatherScreenIntent()
object ShowLessCities : WeatherScreenIntent()
class UpdateGpsStatus(val gpsStatus: GpsStatus) : WeatherScreenIntent()
class UpdateNetworkStatus(val networkStatus: NetworkStatus) : WeatherScreenIntent()
