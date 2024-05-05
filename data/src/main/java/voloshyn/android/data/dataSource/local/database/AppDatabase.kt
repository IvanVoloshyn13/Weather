package voloshyn.android.data.dataSource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.dataSource.local.database.dao.PlaceDao
import voloshyn.android.data.dataSource.local.database.dao.WeatherDao
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

@Database(
    entities = [PlaceEntity::class, CurrentForecastEntity::class,
        HourlyForecastEntity::class, DailyForecastEntity::class,
        PlaceImageEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun weatherDao(): WeatherDao

}