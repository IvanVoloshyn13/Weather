package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val PLACES_TABLE_NAME = "places"

@Entity(tableName = PLACES_TABLE_NAME)
data class PlaceEntity(
    @PrimaryKey
    val id: Int,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",
    val country: String = "",
    val countryCode: String
)