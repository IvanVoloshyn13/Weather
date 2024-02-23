package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.NetworkStatus
import voloshyn.android.domain.model.weather.WeatherComponents

interface WeatherDataRepository {

    suspend fun fetchWeatherForLocation(
        latitude: Double,
        longitude: Double,
        networkStatus: NetworkStatus
    ): Flow<Resource<WeatherComponents>>
}