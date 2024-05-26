package voloshyn.android.weather.presentation.fragment.weather.mvi

import voloshyn.android.domain.NetworkStatus
import voloshyn.android.domain.model.place.Place
import voloshyn.android.weather.gpsReceiver.GpsStatus


sealed class WeatherScreenIntent
object FetchWeatherForCurrentLocation : WeatherScreenIntent()
class FetchWeatherForSavedPlace(val place: Place) : WeatherScreenIntent()
class FetchWeatherForSavedPlaceById(val placeID: Int) : WeatherScreenIntent()
object TogglePlaces : WeatherScreenIntent()
class SetGpsStatus(val gpsStatus: GpsStatus) : WeatherScreenIntent()
class SetCurrentLocationIsActive(val isActive: Boolean) : WeatherScreenIntent()
class UpdateNetworkStatus(val networkStatus: NetworkStatus) : WeatherScreenIntent()
