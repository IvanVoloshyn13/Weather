package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.model.UnsplashImage
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.useCase.toResult

class FetchUnsplashImageByCityNameUseCase(
    private val unsplashImageRepository: UnsplashImageRepository
) {
    suspend fun invoke(cityName: String): UnsplashImage {
        val resource = unsplashImageRepository.fetchUnsplashCityImageByName(cityName)
        val imageUrl = resource.toResult()
        return imageUrl
    }
}