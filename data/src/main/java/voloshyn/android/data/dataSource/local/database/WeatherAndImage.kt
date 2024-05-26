package voloshyn.android.data.dataSource.local.database

import androidx.room.ColumnInfo
import androidx.room.Relation
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

data class WeatherAndImage(
    @ColumnInfo(name = "id") val placeId: Int,
    @Relation(
        parentColumn = "id",
        entityColumn = "place_id",
        entity = HourlyForecastEntity::class
    )
    val hourly: HourlyForecastEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "place_id",
        entity = DailyForecastEntity::class
    )
    val daily: DailyForecastEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "place_id",
        entity = CurrentForecastEntity::class
    )
    val current: CurrentForecastEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "place_id",
        entity = PlaceImageEntity::class
    )
    val imageUrl: PlaceImageEntity,

    )



