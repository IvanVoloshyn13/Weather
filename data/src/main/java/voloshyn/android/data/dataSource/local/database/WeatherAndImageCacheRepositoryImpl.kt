package voloshyn.android.data.dataSource.local.database

import android.util.Log
import voloshyn.android.data.mappers.toCurrentForecast
import voloshyn.android.data.mappers.toDailyForecast
import voloshyn.android.data.mappers.toEntity
import voloshyn.android.data.mappers.toHourlyForecast
import voloshyn.android.data.mappers.toPlaceEntity
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.UnsplashImage
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.model.weather.WeatherComponents
import voloshyn.android.domain.repository.cache.WeatherAndImageCacheRepository
import java.sql.SQLException
import javax.inject.Inject

class WeatherAndImageCacheRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : WeatherAndImageCacheRepository {


    override suspend fun store(placeId: Int, weatherAndImage: WeatherAndImage, place: Place) {
        try {
            db.placeDao().storeNewPlace(place.toPlaceEntity())
            val current = weatherAndImage.weatherComponents.currentForecast.toEntity(placeId)
            val hourly = weatherAndImage.weatherComponents.hourlyForecast.toEntity(placeId)
            val daily = weatherAndImage.weatherComponents.dailyForecast.toEntity(placeId)
            val image = weatherAndImage.image.toEntity(placeId)

            db.weatherDao().store(
                current, hourly, daily, image
            )
        } catch (e: SQLException) {

        }

    }

    override suspend fun get(placeId: Int): WeatherAndImage {
        val weather = db.weatherDao().get(placeId)
        val currentForecast = weather.current.toCurrentForecast()
        val dailyForecast = weather.daily.toDailyForecast()
        val hourlyForecast = weather.hourly.toHourlyForecast()
        val image = weather.imageUrl.imageUrl
        Log.d("WEATHER", weather.toString())

        return WeatherAndImage(
            weatherComponents = WeatherComponents(
                currentForecast,
                hourlyForecast,
                dailyForecast
            ), image = UnsplashImage(image)
        )
    }


}
