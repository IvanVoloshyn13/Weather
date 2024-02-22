package voloshyn.android.network.retrofit.models.unsplash

import com.squareup.moshi.Json

data class UnsplashImageResult(
    @Json(name = "id")
    val imageID: String,
    @Json(name = "urls")
    val imageUrls: UrlsX,
 
)