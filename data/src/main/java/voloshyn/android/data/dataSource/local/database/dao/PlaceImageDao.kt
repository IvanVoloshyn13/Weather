package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity

@Dao
interface PlaceImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(image: PlaceImageEntity)

    @Query("SELECT * from places_image WHERE placeId =:placeId")
    suspend fun get(placeId: Int): PlaceImageEntity
}