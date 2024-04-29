package voloshyn.android.domain.model

import voloshyn.android.domain.model.weather.WeatherComponents

data class WeatherAndImage(
    val weatherComponents: WeatherComponents = WeatherComponents(),
   val image: UnsplashImage=UnsplashImage()
)

