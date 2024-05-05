package voloshyn.android.data.dataSource.local.database

import androidx.room.ColumnInfo
import androidx.room.Relation
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

data class PlaceWithWeather(
    @ColumnInfo(name = "id") val placeId: Int,
    @Relation(
        parentColumn = "id",
        entityColumn = "placeId",
        entity = HourlyForecastEntity::class
    )
    val hourly: HourlyForecastEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "placeId",
        entity = DailyForecastEntity::class
    )
    val daily: DailyForecastEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "placeId",
        entity = CurrentForecastEntity::class
    )
    val current: CurrentForecastEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "placeId",
        entity = PlaceImageEntity::class
    )
    val imageUrl: PlaceImageEntity,

    )



