package voloshyn.android.data.dataSource.local.database

import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.weather.WeatherAndImage

interface WeatherAndImageLocalDataSourceRepository {

    /** Task of this function is to get [weatherAndImage] object and store all the value
     * of it to the corresponding tables in local database */

    //TODO() store place in another way
    suspend fun store(placeId: Int, weatherAndImage: WeatherAndImage, place: Place)

    /** Task of this function is to return [WeatherAndImage] from local database
     * by getting necessary values from corresponding tables using [placeId] and
     *  create [WeatherAndImage] object using this values */
    suspend fun get(placeId:Int): WeatherAndImage
}