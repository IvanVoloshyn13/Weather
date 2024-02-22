package voloshyn.android.network.retrofit.models.weather

data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>
)