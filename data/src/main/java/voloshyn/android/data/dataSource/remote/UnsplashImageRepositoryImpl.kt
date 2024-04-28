package voloshyn.android.data.dataSource.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.domain.model.UnsplashImage
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiUnsplashService
import voloshyn.android.network.retrofit.models.unsplash.UnsplashApiResponse
import voloshyn.android.network.retrofit.utils.UnsplashApi
import javax.inject.Inject
import kotlin.random.Random

class UnsplashImageRepositoryImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UnsplashImageRepository {
    override suspend fun fetchCurrentPlaceImageByName(cityName: String): UnsplashImage =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.imageList.isNotEmpty()) {
                            return@withContext result.data.toCityImage()
                        } else {
                            return@withContext UnsplashImage("")
                        }
                    }

                    is ApiResult.Error -> {
                        throw result.e
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }

    private fun UnsplashApiResponse.toCityImage(): UnsplashImage {
        val randomImageNumber = this.imageList.size.toRandomNumber()
        val imageUrl = this.imageList[randomImageNumber].imageUrls.regular
        return UnsplashImage(url = imageUrl)
    }

    private fun Int.toRandomNumber(): Int {
        return if (this > 0) Random.nextInt(0, this) else 0
    }

}

