package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.weather.WeatherDataRepository


class FetchWeatherDataUseCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend fun invoke(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Flow<Resource<WeatherComponents>> {
        return weatherDataRepository.fetchWeatherForLocation(latitude, longitude, networkStatus)
    }
}