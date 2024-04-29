package voloshyn.android.data.dataSource.remote

<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/repository/weather/UnsplashImageRepositoryImpl.kt
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
=======
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/remote/UnsplashImageRepositoryImpl.kt
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

private const val SMALL_IMAGE = 0
private const val REGULAR_IMAGE = 1

class UnsplashImageRepositoryImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UnsplashImageRepository {
<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/repository/weather/UnsplashImageRepositoryImpl.kt
    private var imageSize: Int = -1
        get() {
            if (field == -1) {  // Check if the value is not initialized
                field = imageSizeByScreenSize()
            }
            return field
        }


    override suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<UnsplashImage> =
=======
    override suspend fun fetchCurrentPlaceImageByName(cityName: String): UnsplashImage =
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/remote/UnsplashImageRepositoryImpl.kt
        withContext(dispatcher) {
            try {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.imageList.isNotEmpty()) {
<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/repository/weather/UnsplashImageRepositoryImpl.kt
                            Resource.Success(data = result.data.toCityImage(imageSize))
=======
                            return@withContext result.data.toCityImage()
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/remote/UnsplashImageRepositoryImpl.kt
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

<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/repository/weather/UnsplashImageRepositoryImpl.kt
    private fun imageSizeByScreenSize(): Int {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.densityDpi
        return when (density) {
            in 0..480 -> {
                SMALL_IMAGE
            }

            else -> REGULAR_IMAGE
        }
=======
    private fun UnsplashApiResponse.toCityImage(): UnsplashImage {
        val randomImageNumber = this.imageList.size.toRandomNumber()
        val imageUrl = this.imageList[randomImageNumber].imageUrls.regular
        return UnsplashImage(url = imageUrl)
    }

    private fun Int.toRandomNumber(): Int {
        return if (this > 0) Random.nextInt(0, this) else 0
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/remote/UnsplashImageRepositoryImpl.kt
    }

}

<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/repository/weather/UnsplashImageRepositoryImpl.kt
fun UnsplashApiResponse.toCityImage(imageSize: Int): UnsplashImage {
    val randomImageNumber = this.imageList.size.toRandomNumber()
    val imageUrl =
        if (imageSize == SMALL_IMAGE) {
            this.imageList[randomImageNumber].imageUrls.small
        } else {
            this.imageList[randomImageNumber].imageUrls.regular

        }
    return UnsplashImage(url = imageUrl)
}

fun Int.toRandomNumber(): Int {
    return if (this > 0) Random.nextInt(0, this) else 0
}


=======
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/remote/UnsplashImageRepositoryImpl.kt
