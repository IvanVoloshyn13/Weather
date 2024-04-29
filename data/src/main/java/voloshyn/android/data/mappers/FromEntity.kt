package voloshyn.android.data.mappers

import android.annotation.SuppressLint
import android.util.Log
import voloshyn.android.data.dataSource.local.database.entities.CurrentForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.DailyForecastEntity
import voloshyn.android.data.dataSource.local.database.entities.HourlyForecastEntity
import voloshyn.android.domain.model.weather.CurrentForecast
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun CurrentForecastEntity.toCurrentForecast(): CurrentForecast {
    return CurrentForecast(
        maxTemperature = this.maxTemperature,
        minTemperature = minTemperature,
        weatherCode = weatherCode,
        currentTemperature = currentTemperature
    )
}

fun DailyForecastEntity.toDailyForecast(): ArrayList<DailyForecast> {
    val dayOfWeek = dayOfTheWeek.split(",")
    val maxTemperature = maxTemperature.split(",")
    val minTemperature = minTemperature.split(",")
    val weatherCode = weatherCode.split(",")
    val dailyList = ArrayList<DailyForecast>()
    dayOfWeek.forEachIndexed { index, s ->
        val daily = DailyForecast(
            weatherCode = weatherCode[index].toInt(),
            maxTemperature = maxTemperature[index].toInt(),
            dayOfTheWeek = s,
            minTemperature = minTemperature[index].toInt()
        )
        dailyList.add(daily)
    }
    return dailyList
}

fun HourlyForecastEntity.toHourlyForecast(): ArrayList<HourlyForecast> {
    val currentTime = currentDate.split(",")
    val temperature = temperature.split(",")
    val weatherCode = weatherCode.split(",")

    val hourlyList = ArrayList<HourlyForecast>()
    currentTime.forEachIndexed { index, s ->
        val hourly = HourlyForecast(
            weatherCode = weatherCode[index].toInt(),
            currentTemp = temperature[index].toInt(),
            currentDate = currentTime[index].toDate()
        )
        hourlyList.add(hourly)
    }
    return hourlyList
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): LocalDateTime {
    // Define the format of the input string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    // Parse the input string into a LocalDateTime object
    val localDateTime = LocalDateTime.parse(this, formatter)
    return localDateTime
}