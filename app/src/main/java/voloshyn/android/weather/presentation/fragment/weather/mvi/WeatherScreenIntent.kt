package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.weather.gpsReceiver.GpsStatus


sealed class WeatherScreenIntent
object FetchWeatherForCurrentLocation : WeatherScreenIntent()
object GetSavedPlaces : WeatherScreenIntent()
class FetchWeatherForSavedPlace(val id: Int) : WeatherScreenIntent()
object ShowMorePlaces : WeatherScreenIntent()
object ShowLessPlaces : WeatherScreenIntent()
class UpdateGpsStatus(val gpsStatus: GpsStatus) : WeatherScreenIntent()
class UpdateNetworkStatus(val networkStatus: NetworkStatus) : WeatherScreenIntent()
