package voloshyn.android.domain.model

private const val EMPTY_CITY_NAME = ""

/**
 * Data class for fused location provider to
 * TODO It will be good to map this after getting weather to [Place] and save in Room with specific Id
 * */
data class CurrentUserLocation(
    val latitude: Double,
    val longitude: Double,
    val city: String,

    ) {
    companion object {
        val DEFAULT = CurrentUserLocation(0.0, 0.0, EMPTY_CITY_NAME)
    }
}
