package voloshyn.android.data.dataSource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.dataSource.local.database.dao.PlaceDao
import voloshyn.android.data.dataSource.local.database.dao.CurrentForecastDao
import voloshyn.android.data.dataSource.local.database.dao.DailyForecastDao
import voloshyn.android.data.dataSource.local.database.dao.HourlyForecastDao
import voloshyn.android.data.dataSource.local.database.dao.PlaceImageDao
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity

@Database(
    entities = [PlaceEntity::class, CurrentForecastEntity::class,
        HourlyForecastEntity::class, DailyForecastEntity::class,
        PlaceImageEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPlaceDao(): PlaceDao
    abstract fun getCurrentForecastDao(): CurrentForecastDao
    abstract fun getDailyForecastDao(): DailyForecastDao
    abstract fun getHourlyForecastDao(): HourlyForecastDao
    abstract fun getPlaceImageDao(): PlaceImageDao
}