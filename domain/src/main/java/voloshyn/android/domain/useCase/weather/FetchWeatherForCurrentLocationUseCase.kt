package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository

class FetchWeatherForCurrentLocationUseCase(private val weatherRepository: CurrentLocationWeatherRepository) {

    suspend fun invoke(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Resource<WeatherComponents> {
        return weatherRepository.fetchWeatherForLocation(latitude, longitude, networkStatus)
    }
}