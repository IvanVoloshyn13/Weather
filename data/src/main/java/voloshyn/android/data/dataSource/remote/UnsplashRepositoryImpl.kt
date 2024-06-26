package voloshyn.android.data.dataSource.remote

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.domain.model.unsplash.UnsplashImage
import voloshyn.android.network.http.utils.executeApiCall
import voloshyn.android.network.retrofit.apiServices.ApiUnsplashService
import voloshyn.android.network.retrofit.models.unsplash.UnsplashApiResponse
import voloshyn.android.network.retrofit.utils.UnsplashApi
import javax.inject.Inject
import kotlin.random.Random

private const val SMALL_IMAGE = 0
private const val REGULAR_IMAGE = 1


class UnsplashRepositoryImpl @Inject constructor(
    @UnsplashApi private val apiUnsplashService: ApiUnsplashService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : UnsplashRepository {

    private var imageSize: Int = -1
        get() {
            if (field == -1) {  // Check if the value is not initialized
                field = imageSizeByScreenSize()
            }
            return field
        }


    override suspend fun fetchImageByName(cityName: String): UnsplashImage =
        withContext(dispatcher) {
            wrapNetworkCallWithException {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                return@wrapNetworkCallWithException result.toCityImage(imageSize) ?: UnsplashImage("")
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


    private fun UnsplashApiResponse.toCityImage(imageSize: Int): UnsplashImage {
        val randomImageNumber = this.imageList.size.toRandomNumber()
        val imageUrl =
            if (imageSize == SMALL_IMAGE) {
                this.imageList[randomImageNumber].imageUrls.small
            } else {
                this.imageList[randomImageNumber].imageUrls.regular

            }
        return UnsplashImage(url = imageUrl)
    }

    private fun Int.toRandomNumber(): Int {
        return if (this > 0) Random.nextInt(0, this) else 0
    }
}


