package voloshyn.android.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePopularPlaces(places: List<PlaceEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewPlace(place: PlaceEntity): Long

    @Query("SELECT * FROM cities")
    suspend fun getAllPlaces(): List<PlaceEntity>

    @Query("SELECT * FROM cities WHERE id=:placeId ")
    suspend fun getPlaceById(placeId: Int): PlaceEntity?


}