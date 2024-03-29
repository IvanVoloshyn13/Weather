package voloshyn.android.http.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public data class NetworkErrorResponse(
    val error: NetworkApiErrorResponse
)

@JsonClass(generateAdapter = true)
public data class NetworkApiErrorResponse(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "description")
    val description: String? = null
)