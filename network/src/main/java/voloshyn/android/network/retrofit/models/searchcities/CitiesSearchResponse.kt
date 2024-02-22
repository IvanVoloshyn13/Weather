package voloshyn.android.network.retrofit.models.searchcities

import com.squareup.moshi.Json

data class CitiesSearchResponse(
    @Json(name = "results")
    val citiesList: List<City> = ArrayList<City>()
)