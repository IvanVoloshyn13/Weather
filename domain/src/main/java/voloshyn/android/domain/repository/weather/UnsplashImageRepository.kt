package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.domain.model.UnsplashImage


interface UnsplashImageRepository {
    /**
     * Load [UnsplashImage] from Unsplash service
     * */
    suspend fun fetchCurrentPlaceImageByName(cityName: String): UnsplashImage
}