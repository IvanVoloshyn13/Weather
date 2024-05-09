package voloshyn.android.domain.model.weather.components

data class DailyForecast(
    val weatherCode: Int,
    val maxTemperature: Int,
    val minTemperature: Int,
    val dayOfTheWeek: String
)
