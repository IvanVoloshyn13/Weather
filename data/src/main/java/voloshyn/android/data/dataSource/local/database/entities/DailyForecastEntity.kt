package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

internal const val DAILY_TABLE = "daily_forecast"

@Entity(
    tableName = DAILY_TABLE,
    indices = [Index("placeId")],
    foreignKeys = [ForeignKey(
        entity = PlaceEntity::class,
        parentColumns = ["id"],
        childColumns = ["placeId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class DailyForecastEntity(
    @PrimaryKey
    val placeId: Int,
    val dayOfTheWeek: StringFromList,
    val maxTemperature: StringFromList,
    val minTemperature: StringFromList,
    val weatherCode: StringFromList
)
