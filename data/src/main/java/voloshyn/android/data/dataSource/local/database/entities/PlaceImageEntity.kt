package voloshyn.android.data.dataSource.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

internal const val IMAGE_TABLE = "places_image"

@Entity(
    tableName = IMAGE_TABLE,
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
data class PlaceImageEntity(
    @PrimaryKey
    val placeId: Int,
    val imageUrl: String
)
