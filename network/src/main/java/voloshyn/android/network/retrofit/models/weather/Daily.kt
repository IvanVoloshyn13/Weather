package voloshyn.android.network.retrofit.models.weather

data class Daily(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_max: List<Double>,
    val weathercode: List<Int>
)