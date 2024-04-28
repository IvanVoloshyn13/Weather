package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity

@Dao
interface CurrentForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(forecast: CurrentForecastEntity)

    @Query("SELECT * from current_forecast WHERE placeId =:placeId")
    suspend fun get(placeId: Int): CurrentForecastEntity
}