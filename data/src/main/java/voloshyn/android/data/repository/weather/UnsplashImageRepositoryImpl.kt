package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.mappers.toResourceError
import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.EmptyBodyError
import voloshyn.android.domain.model.CurrentLocationImage
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.network.http.ApiResult
import voloshyn.android.network.http.exceptions.ApiException
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
    override suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<CurrentLocationImage> =
        withContext(dispatcher) {
            try {
                val result = executeApiCall(
                    call = { apiUnsplashService.fetchPictureByLocation(cityName = cityName) },
                )
                return@withContext when (result) {
                    is ApiResult.Success -> {
                        if (result.data.imageList.isNotEmpty()) {
                            Resource.Success(data = result.data.toCityImage())
                        } else {
                            Resource.Error(e = EmptyBodyError())
                        }
                    }

                    is ApiResult.Error -> {
                        Resource.Error(e = result.e)
                    }
                }
            } catch (e: ApiException) {
                return@withContext e.toResourceError()
            }
        }

}

fun UnsplashApiResponse.toCityImage(): CurrentLocationImage {
    val randomImageNumber = this.imageList.size.toRandomNumber()
    val imageUrl = this.imageList[randomImageNumber].imageUrls.small
    return CurrentLocationImage(cityImageUrl = imageUrl)
}

fun Int.toRandomNumber(): Int {
    return if (this > 0) Random.nextInt(0, this) else 0
}