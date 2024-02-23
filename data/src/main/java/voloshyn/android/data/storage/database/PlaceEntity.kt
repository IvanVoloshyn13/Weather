package voloshyn.android.data.storage.database

import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val CITIES_TABLE_NAME="cities"
@Entity(tableName = CITIES_TABLE_NAME)
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