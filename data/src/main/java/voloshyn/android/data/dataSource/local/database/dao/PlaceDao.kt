package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity


@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePopularPlaces(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeNewPlace(place: PlaceEntity)

    @Query("SELECT * FROM places LIMIT :limitBy")
    fun getAllPlaces(limitBy: Int): Flow<List<PlaceEntity>>

    @Query("SELECT COUNT(*) FROM places ")
    fun placesRowCount(): Int

    @Query("SELECT * FROM places where id=:placeId LIMIT 1")
    fun getPlace(placeId: Int): PlaceEntity


}