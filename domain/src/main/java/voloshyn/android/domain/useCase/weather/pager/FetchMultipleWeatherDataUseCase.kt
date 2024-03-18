package voloshyn.android.domain.useCase.weather.pager

import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.pager.WeatherDataRepository
import voloshyn.android.domain.useCase.toResult

class FetchMultipleWeatherDataUseCase(private val repository: WeatherDataRepository) {
    suspend fun invoke(latitudeLongitude: LatitudeLongitude): List<WeatherComponents> {
        val result = repository.fetchWeather(
            latitude = latitudeLongitude.latitudeArray,
            longitude = latitudeLongitude.longitudeArray
        ).toResult()
        return result
    }
}