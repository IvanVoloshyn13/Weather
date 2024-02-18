package voloshyn.android.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import voloshyn.android.data.di.CITIES_TABLE_NAME
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