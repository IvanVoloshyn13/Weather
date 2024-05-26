package voloshyn.android.data.dataSource.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import voloshyn.android.data.dataSource.local.database.dao.PlaceDao
import voloshyn.android.data.dataSource.local.database.dao.WeatherAndImageDao
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

@Database(
    entities = [PlaceEntity::class, CurrentForecastEntity::class,
        HourlyForecastEntity::class, DailyForecastEntity::class,
        PlaceImageEntity::class], version = 2, exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun weatherAndImageDao(): WeatherAndImageDao

}