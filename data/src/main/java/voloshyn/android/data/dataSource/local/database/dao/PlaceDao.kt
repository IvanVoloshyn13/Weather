package voloshyn.android.data.dataSource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
<<<<<<< HEAD:data/src/main/java/voloshyn/android/data/storage/database/PlaceDao.kt
import voloshyn.android.domain.useCase.weather.pager.LatitudeLongitude
import javax.xml.transform.dom.DOMLocator
=======
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.dataSource.local.database.entities.PlaceEntity
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca:data/src/main/java/voloshyn/android/data/dataSource/local/database/dao/PlaceDao.kt

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePopularPlaces(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeNewPlace(place: PlaceEntity)

    @Query("SELECT * FROM places")
     fun getAllPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id=:placeId ")
    suspend fun getPlaceById(placeId: Int): PlaceEntity?

    @Query("SELECT latitude FROM places")
    suspend fun getLatitude(): List<Double>

    @Query("SELECT longitude FROM places")
    suspend fun getLongitude(): List<Double>


}