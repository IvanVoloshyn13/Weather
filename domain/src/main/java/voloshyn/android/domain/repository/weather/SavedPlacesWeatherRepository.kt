package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.model.NetworkStatus

interface SavedPlacesWeatherRepository {
    suspend fun fetchWeather(
        placeId: Int,
        networkStatus: NetworkStatus
    )
}