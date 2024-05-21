package voloshyn.android.domain.repository

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.model.place.Place
import voloshyn.android.domain.model.weather.WeatherAndImage

interface WeatherAndImageRepository {

    /**
     * The main method to get necessary weather and image data.
     * The method [get] first will be try to get all data from network: [UnsplashRepository.fetchCurrentPlaceImageByName],
     * [WeatherRepository.fetchWeather]. If it will be success, we first
     * save new data in local database and after that show it in our UI. If response return Error when we get
     * old data and show error message or dialog about error while updating data.
     * So our scenario is to have only one data source - Local database
     * @param [place]
     * */

    // TODO create tuples instead Place , PlaceTuples(id,name)
    suspend fun get(place: Place): AppResult<WeatherAndImage, DataError>
}