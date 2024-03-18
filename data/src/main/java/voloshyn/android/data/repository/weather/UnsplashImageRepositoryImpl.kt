package voloshyn.android.data.repository.weather

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.R
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.EmptyBodyError
import voloshyn.android.domain.model.UnsplashImage
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiUnsplashService
import voloshyn.android.network.retrofit.models.unsplash.UnsplashApiResponse
import voloshyn.android.network.retrofit.utils.UnsplashApi
import javax.inject.Inject
import kotlin.random.Random

private const val SMALL_IMAGE = 0
private const val REGULAR_IMAGE = 1

class UnsplashImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UnsplashImageRepository {
    private var imageSize: Int = -1
        get() {
            if (field == -1) {  // Check if the value is not initialized
                field = imageSizeByScreenSize()
            }
            return field
        }


    override suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<UnsplashImage> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                return@withContext when (result) {
                    is ApiResult.Success -> {
                        if (result.data.imageList.isNotEmpty()) {
                            Resource.Success(data = result.data.toCityImage(imageSize))
                        } else {
                            Resource.Error(e = EmptyBodyError(context.getString(R.string.data_not_found)))
                        }
                    }

                    is ApiResult.Error -> {
                        Resource.Error(e = result.e)
                    }
                }
            } catch (e: ApiException) {
                return@withContext e.toResourceError(context)
            }
        }

    private fun imageSizeByScreenSize(): Int {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.densityDpi
        return when (density) {
            in 0..480 -> {
                SMALL_IMAGE
            }

            else -> REGULAR_IMAGE
        }
    }

}

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


