package voloshyn.android.domain.model

private const val EMPTY_STRING = ""

/**
 * Data class for the place that will be use in Ui layer in recycler and for getting
 * weather and image
 * */
data class Place(
    val id: Int = 0,
    val name: String = EMPTY_STRING,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = EMPTY_STRING,
    val country: String="",
    val countryCode: String = ""
) {

    override fun toString(): String {
        return "$name, $country"
    }

    companion object {
        private const val ERROR_LOCATION = "Error while getting location"
        val EMPTY_PLACE_ERROR = Place(
            id = 0,
            name = ERROR_LOCATION,
            latitude = 0.0,
            longitude = 0.0,
            timezone = EMPTY_STRING,
            country = EMPTY_STRING,
            countryCode = EMPTY_STRING
        )
    }
}

enum class ListSizeState {
    FULL, TRIM, DEFAULT
}
