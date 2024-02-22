package voloshyn.android.weather.presentation.fragment.weather

data class WeatherState(
    val location: String = "Location",
    val currentTime: String = "00:00",
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = ""
)