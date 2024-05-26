package voloshyn.android.network.retrofit.models.weather

import com.squareup.moshi.Json

data class Daily(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    @Json(name = "time")
    val date: List<String>,
    val uv_index_max: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>
)