package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity.DailyForecastTable.NAME.DAILY_TABLE


@Entity(
    tableName = DAILY_TABLE,
    indices = [Index("place_id")],
    foreignKeys = [ForeignKey(
        entity = PlaceEntity::class,
        parentColumns = ["id"],
        childColumns = ["place_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class DailyForecastEntity(
    @PrimaryKey
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "day_of_the_week") val dayOfTheWeek: StringFromList,
    @ColumnInfo(name = "max_temperature") val maxTemperature: StringFromList,
    @ColumnInfo(name = "min_temperature") val minTemperature: StringFromList,
    @ColumnInfo(name = "weather_code") val weatherCode: StringFromList
) {
    interface DailyForecastTable {
        companion object NAME {
            const val DAILY_TABLE = "daily_forecast"
        }

        object Columns {
            const val PLACE_ID = "place_id"
            const val DAY_OF_THE_WEEK = "day_of_the_week"
            const val MAX_TEMP = "max_temperature"
            const val MIN_TEMP = "min_temperature"
            const val WEATHER_CODE = "weather_code"
        }
    }

}
