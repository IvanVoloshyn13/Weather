package voloshyn.android.domain.model.weather

import java.time.LocalDateTime


data class HourlyForecast(
    val currentDate: LocalDateTime,
    val currentTemp: Int,
    val weatherCode: Int
)
