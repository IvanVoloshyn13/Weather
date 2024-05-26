package voloshyn.android.data.nameLater


import voloshyn.android.domain.model.weather.components.WeatherComponents
import voloshyn.android.network.retrofit.models.weather.WeatherResponse

interface WeatherComponentsCreator {
    fun toWeatherComponents(response: WeatherResponse): WeatherComponents

}