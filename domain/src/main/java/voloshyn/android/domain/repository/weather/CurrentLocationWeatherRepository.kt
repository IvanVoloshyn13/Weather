package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents

interface CurrentLocationWeatherRepository {

    suspend fun fetchWeatherForLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Resource<WeatherComponents>
}