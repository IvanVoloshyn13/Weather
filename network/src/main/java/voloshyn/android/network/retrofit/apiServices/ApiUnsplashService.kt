package voloshyn.android.network.retrofit.apiServices

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import voloshyn.android.network.retrofit.models.unsplash.UnsplashApiResponse
import voloshyn.android.network.retrofit.utils.Unsplash

interface ApiUnsplashService {
    @GET("/search/photos")
    @Headers("Accept-Version: v1")
    suspend fun fetchPictureByLocation(
        @Query("client_id") clientId: String= Unsplash.CLIENT_ID,
        @Query("query") cityName: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("orientation") orientation: String = "portrait",
    ): Response<UnsplashApiResponse>
}