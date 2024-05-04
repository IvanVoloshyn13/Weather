package voloshyn.android.domain.repository.cache

import voloshyn.android.domain.model.Place
import voloshyn.android.domain.model.WeatherAndImage

interface WeatherAndImageCacheRepository {

    /** Task of this function is to get [weatherAndImage] object and store all the value
     * of it to the corresponding tables in local database */

    //TODO() store place in another way
    //Dont remember for what i wrote this todo()
    suspend fun store(placeId: Int,weatherAndImage: WeatherAndImage,place:Place)

    /** Task of this function is to return [WeatherAndImage] from local database
     * by getting necessary values from corresponding tables using [placeId] and
     *  create [WeatherAndImage] object using this values */
    suspend fun get(placeId:Int): WeatherAndImage
}