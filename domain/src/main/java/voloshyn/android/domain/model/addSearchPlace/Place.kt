package voloshyn.android.domain.model.addSearchPlace

data class Place(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val country: String,
    val countryCode: String = ""
) {

    override fun toString(): String {
        return "$name, $country"
    }


}
