package voloshyn.android.data.dataSource.remote

import voloshyn.android.domain.model.weather.WeatherAndImage
import voloshyn.android.domain.model.weather.components.WeatherComponents

interface WeatherRepository {

    /** Load [WeatherComponents] from OpenMeteo service for the current location the user wants
     * to check the weather for. */
    suspend fun fetchWeather(latitude: Double, longitude: Double): WeatherComponents
}