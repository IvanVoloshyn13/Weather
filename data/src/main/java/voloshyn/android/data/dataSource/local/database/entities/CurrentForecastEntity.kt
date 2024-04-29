package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

internal const val CURRENT_FORECAST_TABLE = "current_forecast"

@Entity(
    tableName = CURRENT_FORECAST_TABLE,
    indices = [Index("placeId")],
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CurrentForecastEntity(
    @PrimaryKey
    val placeId: Int,
    val currentTemperature: Int,
    val maxTemperature: Int,
    val minTemperature: Int,
    val weatherCode: Int
)
