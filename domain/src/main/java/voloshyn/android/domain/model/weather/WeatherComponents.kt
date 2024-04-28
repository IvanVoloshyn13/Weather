package voloshyn.android.domain.model.weather

data class WeatherComponents(
    val currentForecast: CurrentForecast = CurrentForecast(),
    val hourlyForecast: List<HourlyForecast> = ArrayList(),
    val dailyForecast: List<DailyForecast> = ArrayList(),
    val timezone: String? = ""
)
