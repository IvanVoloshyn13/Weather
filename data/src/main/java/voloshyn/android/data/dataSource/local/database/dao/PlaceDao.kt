package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity.PlacesTable.NAME.PLACES_TABLE_NAME


@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePlaces(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePlace(place: PlaceEntity)

    @Query("SELECT * FROM places LIMIT :limitBy")
   suspend fun getPlaces(limitBy: Int): List<PlaceEntity>

    @Query("SELECT COUNT(*) FROM places ")
    fun placesRowCount(): Flow<Int>

    @Query("SELECT * FROM $PLACES_TABLE_NAME where places.id =:placeId LIMIT 1")
   suspend fun getPlace(placeId: Int): PlaceEntity

    @Query("SELECT id FROM places where id =:id")
    suspend fun placeExist(id: Int): Int

}