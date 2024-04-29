package voloshyn.android.data.dataSource.local.database

import android.util.Log
import voloshyn.android.data.mappers.toEntity
import voloshyn.android.data.mappers.toPlaceEntity
import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage
import voloshyn.android.domain.repository.cache.WeatherAndImageCacheRepository
import java.sql.SQLException
import javax.inject.Inject

class WeatherAndImageCacheRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : WeatherAndImageCacheRepository {


    override suspend fun store(placeId: Int, weatherAndImage: WeatherAndImage,place: Place) {
        try {
            db.getPlaceDao().storeNewPlace(place.toPlaceEntity())
            db.getCurrentForecastDao()
                .store(weatherAndImage.weatherComponents.currentForecast.toEntity(placeId))
            db.getDailyForecastDao()
                .store(weatherAndImage.weatherComponents.dailyForecast.toEntity(placeId))
            Log.d("CACHE", weatherAndImage.weatherComponents.dailyForecast[0].toString())
            db.getHourlyForecastDao()
                .store(weatherAndImage.weatherComponents.hourlyForecast.toEntity(placeId))
            Log.d("CACHE1", weatherAndImage.weatherComponents.hourlyForecast[0].toString())
            db.getPlaceImageDao().store(weatherAndImage.image.toEntity(placeId))
            Log.d("CACHE", weatherAndImage.image.url)
        } catch (e: SQLException) {

        }

    }

    override suspend fun get(placeId: Int): WeatherAndImage {
        TODO("Not yet implemented")
    }


}
