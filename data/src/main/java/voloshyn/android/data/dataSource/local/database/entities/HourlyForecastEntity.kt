package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity.Table.NAME.HOURLY_TABLE


typealias StringFromList = String

@Entity(
    tableName = HOURLY_TABLE,
    indices = [Index("place_id")],
    foreignKeys = [ForeignKey(
        entity = PlaceEntity::class,
        parentColumns = ["id"],
        childColumns = ["place_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class HourlyForecastEntity(
    @PrimaryKey
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "current_date") val currentDate: StringFromList,
    @ColumnInfo(name = "temperature") val temperature: StringFromList,
    @ColumnInfo(name = "weather_code") val weatherCode: StringFromList
) {
    interface Table {
        companion object NAME {
            const val HOURLY_TABLE = "hourly_forecast"
        }

        object Columns {
            const val PLACE_ID = "place_id"
            const val CURRENT_DATE = "current_date"
            const val TEMPERATURE = "temperature"
            const val WEATHER_CODE = "weather_code"
        }
    }

}