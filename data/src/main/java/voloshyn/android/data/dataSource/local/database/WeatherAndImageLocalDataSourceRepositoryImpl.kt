package voloshyn.android.data.dataSource.local.database

import voloshyn.android.data.dataSource.local.CustomSqlException
import voloshyn.android.data.dataSource.local.database.dao.PlaceDao
import voloshyn.android.data.dataSource.local.database.dao.WeatherAndImageDao
import voloshyn.android.data.mappers.toCurrentForecast
import voloshyn.android.data.mappers.toDailyForecast
import voloshyn.android.data.mappers.toEntity
import voloshyn.android.data.mappers.toHourlyForecast
import voloshyn.android.data.mappers.toPlaceEntity
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.unsplash.UnsplashImage
import voloshyn.android.domain.model.weather.WeatherAndImage
import voloshyn.android.domain.model.weather.components.WeatherComponents
import java.sql.SQLException
import javax.inject.Inject

class WeatherAndImageLocalDataSourceRepositoryImpl @Inject constructor(
    private val weatherAndImageDao: WeatherAndImageDao,
    private val placeDao: PlaceDao
) : WeatherAndImageLocalDataSourceRepository {


    override suspend fun store(placeId: Int, weatherAndImage: WeatherAndImage, place: Place) {
        try {
         //   placeDao.storePlace(place.toPlaceEntity())
            val current = weatherAndImage.weatherComponents.currentForecast.toEntity(placeId)
            val hourly = weatherAndImage.weatherComponents.hourlyForecast.toEntity(placeId)
            val daily = weatherAndImage.weatherComponents.dailyForecast.toEntity(placeId)
            val image = weatherAndImage.image.toEntity(placeId)

            weatherAndImageDao.store(
                current, hourly, daily, image
            )
        } catch (e: SQLException) {
            throw e
        }

    }

    override suspend fun get(placeId: Int): WeatherAndImage {
        if (placeDao.placeExist(placeId).compareTo(placeId) != 0 ||
            weatherAndImageDao.weatherExist(placeId).compareTo(placeId) != 0
        ) {
            throw CustomSqlException.NoSuchPlaceException
        }
        val weather =
            weatherAndImageDao.get(placeId)
        val currentForecast =
            weather.current.toCurrentForecast()
        val dailyForecast =
            weather.daily.toDailyForecast()
        val hourlyForecast =
            weather.hourly.toHourlyForecast()
        val image = weather.imageUrl.imageUrl

        return WeatherAndImage(
            weatherComponents = WeatherComponents(
                currentForecast,
                hourlyForecast,
                dailyForecast
            ), image = UnsplashImage(image)
        )
    }


}
