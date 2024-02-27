package voloshyn.android.network.retrofit.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchedPlaces(
    val id: Int = 0,
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val timezone: String = "",
    val countyCode: String = ""
)