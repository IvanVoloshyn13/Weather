package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository
import voloshyn.android.domain.useCase.toResult

class FetchWeatherForCurrentLocationUseCase(private val weatherRepository: CurrentLocationWeatherRepository) {

    suspend fun invoke(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): WeatherComponents {
        return weatherRepository.fetchWeatherForLocation(latitude, longitude, networkStatus).toResult()
    }
}