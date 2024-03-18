package voloshyn.android.domain.repository.weather.pager

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.weather.WeatherComponents

interface WeatherDataRepository {
    suspend fun fetchWeather(
        latitude: DoubleArray,
        longitude: DoubleArray
    ): Resource<List<WeatherComponents>>
}