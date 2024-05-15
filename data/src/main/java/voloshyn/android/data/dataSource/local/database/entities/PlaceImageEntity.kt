package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity.ImagesTable.NAME.IMAGE_TABLE


@Entity(
    tableName = IMAGE_TABLE,
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
data class PlaceImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String
) {
     interface ImagesTable {
         companion object NAME{
             const val IMAGE_TABLE = "places_image"
         }
         object Columns {
            const val PLACE_ID = "place_id"
            const val IMAGE_URL = "image_url"

        }
    }
}
