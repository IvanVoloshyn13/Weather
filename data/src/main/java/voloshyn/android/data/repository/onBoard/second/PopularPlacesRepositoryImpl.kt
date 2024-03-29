package voloshyn.android.data.repository.onBoard.second

import android.util.Log
import voloshyn.android.data.storage.database.AppDatabase
import voloshyn.android.data.storage.database.PlaceEntity
import voloshyn.android.domain.model.onBoarding.PopularPlace
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import javax.inject.Inject

class PopularPlacesRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : PopularPlacesRepository {

    override suspend fun savePlaces(places: Array<PopularPlace>): List<Long> {
        val resultLong = db.getCityDao().savePopularPlaces(places = places.toPlaceEntityArray())
        resultLong.forEach {
            Log.d("DATABASE", it.toString())
        }

        return resultLong
    }
}

fun Array<PopularPlace>.toPlaceEntityArray(): List<PlaceEntity> {
    return this.map {
        PlaceEntity(
            id = it.id,
            name = it.name,
            latitude = it.latitude,
            longitude = it.longitude,
            timezone = it.timezone,
            country = it.country,
            countryCode = it.countryCode
        )
    }
}