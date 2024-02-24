package voloshyn.android.weather.presentation.fragment.weather


sealed class WeatherScreenIntent
object GetWeatherByCurrentLocation : WeatherScreenIntent()
object GetSavedLocationsList : WeatherScreenIntent()
class GetLocationById(val cityId: Int) : WeatherScreenIntent()
object ShowMoreCities : WeatherScreenIntent()
object ShowLessCities : WeatherScreenIntent()
