package voloshyn.android.data.dataSource.remote

import voloshyn.android.domain.model.unsplash.UnsplashImage


interface UnsplashRepository {

    /** Load [UnsplashImage] from Unsplash service for the current location the user wants to
     *  check the weather for. */
    suspend fun fetchImageByName(cityName: String): UnsplashImage
}