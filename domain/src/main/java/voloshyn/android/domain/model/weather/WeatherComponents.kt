package voloshyn.android.domain.model.weather

data class WeatherComponents(
    val mainWeatherInfo: MainWeatherInfo = MainWeatherInfo(),
    val hourlyForecast: List<HourlyForecast> = ArrayList(),
    val dailyForecast: List<DailyForecast> = ArrayList(),
    val timezone: String? = "",
    )

