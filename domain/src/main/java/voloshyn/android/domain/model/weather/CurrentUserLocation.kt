package voloshyn.android.domain.model.weather

data class CurrentUserLocation(
    val latitude: Double,
    val longitude: Double,
    val timeZoneID: String,
    val city: String,

) {
    companion object {
        val DEFAULT = CurrentUserLocation(0.0, 0.0, "", "")
    }
}
