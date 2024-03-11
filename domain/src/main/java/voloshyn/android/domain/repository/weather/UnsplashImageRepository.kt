package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.UnsplashImage

interface UnsplashImageRepository {
    suspend fun fetchUnsplashCityImageByName(cityName: String): Resource<UnsplashImage>
}