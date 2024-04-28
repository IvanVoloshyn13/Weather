package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity

@Dao
interface DailyForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(forecast: DailyForecastEntity)

    @Query("SELECT * from daily_forecast WHERE placeId =:placeId")
    suspend fun get(placeId: Int): DailyForecastEntity
}