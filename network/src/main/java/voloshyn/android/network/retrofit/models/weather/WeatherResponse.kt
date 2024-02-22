package voloshyn.android.network.retrofit.models.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val daily: Daily,
    val hourly: Hourly,
    val timezone: String
)