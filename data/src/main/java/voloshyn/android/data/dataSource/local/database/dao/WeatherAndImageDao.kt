package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import voloshyn.android.data.dataSource.local.database.PlaceWithWeather
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

@Dao
interface WeatherAndImageDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun store(
        current: CurrentForecastEntity,
        hourly: HourlyForecastEntity,
        daily: DailyForecastEntity,
        image: PlaceImageEntity
    )


    @Query("SELECT places.id \n" +
            " FROM places \n" +
            " LEFT JOIN hourly_forecast ON places.id = hourly_forecast.placeId \n " +
            " LEFT JOIN daily_forecast ON places.id = daily_forecast.placeId \n" +
            " LEFT JOIN current_forecast ON places.id = current_forecast.placeId \n" +
            " WHERE places.id =:placeId ")
    fun get(placeId: Int): PlaceWithWeather
}