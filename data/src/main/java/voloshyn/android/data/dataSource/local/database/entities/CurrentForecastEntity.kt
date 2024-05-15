package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity.CurrentForecastTable.NAME.CURRENT_FORECAST_TABLE


@Entity(
    tableName = CURRENT_FORECAST_TABLE,
    indices = [Index("place_id")],
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CurrentForecastEntity(
    @PrimaryKey
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "current_temperature") val currentTemperature: Int,
    @ColumnInfo(name = "max_temperature") val maxTemperature: Int,
    @ColumnInfo(name = "min_temperature") val minTemperature: Int,
    @ColumnInfo(name = "weather_code") val weatherCode: Int
) {

    interface CurrentForecastTable {
        companion object NAME {
            const val CURRENT_FORECAST_TABLE = "current_forecast"
        }

        object Columns {
            const val PLACE_ID = "place_id"
            const val CURRENT_TEMP = "current_temperature"
            const val MAX_TEMP = "max_temperature"
            const val MIN_TEMP = "min_temperature"
            const val WEATHER_CODE = "weather_code"
        }
    }


}
