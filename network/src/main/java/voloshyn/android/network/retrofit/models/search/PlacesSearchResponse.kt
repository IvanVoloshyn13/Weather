package voloshyn.android.network.retrofit.models.search

import com.squareup.moshi.Json

data class PlacesSearchResponse(
    @Json(name = "results")
    val citiesList: List<SearchedPlaces> = ArrayList<SearchedPlaces>()
)