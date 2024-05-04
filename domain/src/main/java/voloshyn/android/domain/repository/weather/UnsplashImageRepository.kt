package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.model.UnsplashImage


interface UnsplashImageRepository {
    /** Load [UnsplashImage] from Unsplash service */
    suspend fun fetchCurrentPlaceImageByName(cityName: String): UnsplashImage
}