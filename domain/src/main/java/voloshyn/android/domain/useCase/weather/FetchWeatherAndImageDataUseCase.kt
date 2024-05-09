package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.weather.WeatherAndImage
import voloshyn.android.domain.repository.WeatherAndImageRepository

class FetchWeatherAndImageDataUseCase(private val repository: WeatherAndImageRepository) {
    suspend fun invoke(place: Place): AppResult<WeatherAndImage, DataError> {
        val appResult = repository.get(place)
        return appResult
    }
}