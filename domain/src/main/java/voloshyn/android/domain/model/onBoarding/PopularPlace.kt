package voloshyn.android.domain.model.onBoarding

data class PopularPlace(
    val id: Int = 0,
    val name: String = "",
    val country: String = "",
    val countryCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = ""
)