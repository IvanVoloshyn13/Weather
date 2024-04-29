package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.error.AppError
import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository

class FetchWeatherAndImageDataUseCase(private val repository: FetchWeatherAndImageRepository) {
    suspend fun invoke(place: Place): AppResult<WeatherAndImage, AppError> {
        val appResult = repository.get(place)
        return appResult
    }
}