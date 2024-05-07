package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository

class FetchWeatherAndImageDataUseCase(private val repository: FetchWeatherAndImageRepository) {
    suspend fun invoke(place: Place): AppResult<WeatherAndImage, DataError> {
        val appResult = repository.get(place)
        return appResult
    }
}