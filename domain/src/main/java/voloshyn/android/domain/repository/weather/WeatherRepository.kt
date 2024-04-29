package voloshyn.android.domain.repository.weather

import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.model.weather.WeatherComponents

interface WeatherRepository {

    /**
     * Load [WeatherAndImage] from OpenMeteo Api service
     * */
    suspend fun fetchWeather(latitude: Double, longitude: Double): WeatherComponents
}