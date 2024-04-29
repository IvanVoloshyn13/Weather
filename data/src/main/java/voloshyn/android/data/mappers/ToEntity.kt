package voloshyn.android.data.mappers

import android.util.Log
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.PlaceImageEntity
import voloshyn.android.domain.model.UnsplashImage
import voloshyn.android.domain.model.weather.CurrentForecast
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast

fun CurrentForecast.toEntity(placeId: Int): CurrentForecastEntity {
    return CurrentForecastEntity(
        placeId = placeId,
        currentTemperature = currentTemperature,
        maxTemperature = maxTemperature,
        minTemperature = minTemperature,
        weatherCode = weatherCode
    )
}

fun List<HourlyForecast>.toEntity(placeId: Int): HourlyForecastEntity {
    val currentDayArray: Array<String> = Array(size) { "" }
    val temperatureArray: Array<String> = Array(size) {
        ""
    }
    val weatherCodeArray: IntArray = IntArray(size)
    this.forEachIndexed { index, hourlyForecast ->
        currentDayArray.set(index = index, value = hourlyForecast.currentDate.toString())
        temperatureArray[index] = hourlyForecast.currentTemp.toString()
        weatherCodeArray[index] = hourlyForecast.weatherCode
    }
    return HourlyForecastEntity(
        placeId,
        currentDate = currentDayArray.joinToString(separator = ","),
        temperature = temperatureArray.joinToString(separator = ","),
        weatherCode = weatherCodeArray.joinToString(separator = ",")
    )
}

fun List<DailyForecast>.toEntity(placeId: Int): DailyForecastEntity {
    val dayOfTheWeekArray: Array<String> = Array(size) { "" }
    val maxTemperatureArray: Array<String> = Array(size) { "" }
    val minTemperatureArray: Array<String> = Array(size) { "" }
    val weatherCodeArray: IntArray = IntArray(size) { 0 }
    this.forEachIndexed { index, daily ->
        dayOfTheWeekArray.set(index = index, value = daily.dayOfTheWeek)
        maxTemperatureArray[index] = daily.maxTemperature.toString()
        minTemperatureArray[index] = daily.minTemperature.toString()
        weatherCodeArray[index] = daily.weatherCode
    }
    return DailyForecastEntity(
        placeId,
        dayOfTheWeek = dayOfTheWeekArray.joinToString(separator = ","),
        maxTemperature = maxTemperatureArray.joinToString(separator = ","),
        minTemperature = minTemperatureArray.joinToString(separator = ","),
        weatherCode = weatherCodeArray.joinToString(separator = ",")
    )
}

fun UnsplashImage.toEntity(placeId: Int): PlaceImageEntity {
    return PlaceImageEntity(
        placeId = placeId,
        imageUrl = this.url
    )
}

