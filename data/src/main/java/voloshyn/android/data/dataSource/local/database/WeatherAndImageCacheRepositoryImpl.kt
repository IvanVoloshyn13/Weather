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
            db.getPlaceDao().storeNewPlace(place.toPlaceEntity())
            db.getCurrentForecastDao()
                .store(weatherAndImage.weatherComponents.currentForecast.toEntity(placeId))
            db.getDailyForecastDao()
                .store(weatherAndImage.weatherComponents.dailyForecast.toEntity(placeId))
            db.getHourlyForecastDao()
                .store(weatherAndImage.weatherComponents.hourlyForecast.toEntity(placeId))
            db.getPlaceImageDao().store(weatherAndImage.image.toEntity(placeId))
        } catch (e: SQLException) {

        }

    }

    override suspend fun get(placeId: Int): WeatherAndImage {
        val currentForecast = db.getCurrentForecastDao().get(placeId).toCurrentForecast()
        val dailyForecast = db.getDailyForecastDao().get(placeId).toDailyForecast()
        val hourlyForecast = db.getHourlyForecastDao().get(placeId).toHourlyForecast()
        val image = db.getPlaceImageDao().get(placeId).imageUrl
        return WeatherAndImage(
            weatherComponents = WeatherComponents(
                currentForecast,
                hourlyForecast,
                dailyForecast
            ), image = UnsplashImage(image)
        )
    }


}
