package voloshyn.android.data.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voloshyn.android.domain.useCase.weather.pager.LatitudeLongitude
import javax.xml.transform.dom.DOMLocator

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePopularPlaces(places: List<PlaceEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewPlace(place: PlaceEntity): Long

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces(): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE id=:placeId ")
    suspend fun getPlaceById(placeId: Int): PlaceEntity?

    @Query("SELECT latitude FROM places")
    suspend fun getLatitude(): List<Double>

    @Query("SELECT longitude FROM places")
    suspend fun getLongitude(): List<Double>


}