package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.weather.gpsReceiver.GpsStatus


sealed class WeatherScreenIntent
object FetchWeatherForCurrentLocation : WeatherScreenIntent()
class FetchWeatherForSavedPlace(val id: Int) : WeatherScreenIntent()
object TogglePlaces : WeatherScreenIntent()
class UpdateGpsStatus(val gpsStatus: GpsStatus) : WeatherScreenIntent()
class UpdateNetworkStatus(val networkStatus: NetworkStatus) : WeatherScreenIntent()
