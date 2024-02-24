package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.repository.weather.UnsplashImageRepository

class FetchUnsplashImageByCityNameUseCase(
    private val unsplashImageRepository: UnsplashImageRepository
) {
    suspend fun invoke(cityName: String) =
        unsplashImageRepository.fetchUnsplashCityImageByName(cityName)
}