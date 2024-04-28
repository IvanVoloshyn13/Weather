package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
@Dao
interface HourlyForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(forecast: HourlyForecastEntity)

    @Query("SELECT * from hourly_forecast WHERE placeId =:placeId")
    suspend fun get(placeId: Int): HourlyForecastEntity
}