package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dagger.Provides

internal const val HOURLY_TABLE = "hourly_forecast"

typealias StringFromList = String

@Entity(
    tableName = HOURLY_TABLE,
    indices = [Index("placeId")],
    foreignKeys = [ForeignKey(
        entity = PlaceEntity::class,
        parentColumns = ["id"],
        childColumns = ["placeId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class HourlyForecastEntity(
    @PrimaryKey
    val placeId: Int,
    val currentDate: StringFromList,
    val temperature: StringFromList,
    val weatherCode: StringFromList
){

}