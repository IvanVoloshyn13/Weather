package voloshyn.android.domain.model.place


/** Data class for fused location provider */
data class CurrentUserLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = EMPTY_STRING,

    )
