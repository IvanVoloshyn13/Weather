package voloshyn.android.domain.model.weather

import voloshyn.android.domain.model.unsplash.UnsplashImage
import voloshyn.android.domain.model.weather.components.WeatherComponents

data class WeatherAndImage(
    val weatherComponents: WeatherComponents = WeatherComponents(),
    val image: UnsplashImage = UnsplashImage()
)

