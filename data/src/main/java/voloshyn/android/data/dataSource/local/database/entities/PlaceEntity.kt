package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity.PlacesTable.NAME.PLACES_TABLE_NAME


@Entity(tableName = PLACES_TABLE_NAME)
data class PlaceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "timezone") val timezone: String = "",
    @ColumnInfo(name = "country") val country: String = "",
    @ColumnInfo(name = "country_code") val countryCode: String
) {
    interface PlacesTable {

        companion object NAME {
            const val PLACES_TABLE_NAME = "places"
        }

        object Columns {
            const val ID = "id"
            const val NAME = "name"
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
            const val TIMEZONE = "timezone"
            const val COUNTRY = "country"
            const val COUNTRY_CODE = "country_code"
        }
    }
}