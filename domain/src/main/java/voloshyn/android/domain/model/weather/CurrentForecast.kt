package voloshyn.android.domain.model.weather

data class CurrentForecast(
    val maxTemperature: Int = 0,
    val minTemperature: Int = 0,
    val weatherCode: Int = 1,
    val currentTemperature: Int = 0,
)

