package voloshyn.android.data.nameLater

import voloshyn.android.domain.model.weather.components.CurrentForecast
import voloshyn.android.domain.model.weather.components.DailyForecast
import voloshyn.android.domain.model.weather.components.HourlyForecast
import voloshyn.android.domain.model.weather.components.WeatherComponents
import voloshyn.android.network.retrofit.models.weather.Daily
import voloshyn.android.network.retrofit.models.weather.Hourly
import voloshyn.android.network.retrofit.models.weather.WeatherResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Stream
import javax.inject.Inject

class WeatherComponentsCreatorImpl @Inject constructor() : WeatherComponentsCreator {
    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    override fun toWeatherComponents(response: WeatherResponse): WeatherComponents {
        val dailyForecast = toDailyForecast(response.daily)[0] as ArrayList<DailyForecast>
        val hourlyForecast = toHourlyForecastList(response)
        var todayMaxTemp: Int = 0
        var todayMinTemp: Int = 0
        var currentHourWeatherCode: Int = 0
        var currentHourTemp: Int = 0
        dailyForecast[0].let {
            todayMaxTemp = it.maxTemperature
            todayMinTemp = it.minTemperature
        }
        val timeZoneId = response.timezone
        val time = dateTimeHelper.run { currentLocalTime(timeZoneId) }
        val currentHourWeather = hourlyForecast.firstOrNull() { hourly ->
            hourly.currentDate.hour == time
        }
        if (currentHourWeather != null) {
            currentHourTemp = currentHourWeather.currentTemp
            currentHourWeatherCode = currentHourWeather.weatherCode
        }

        return WeatherComponents(
            dailyForecast = dailyForecast ,
            hourlyForecast =hourlyForecast,
            currentForecast = CurrentForecast(
                todayMaxTemp,
                todayMinTemp,
                currentHourWeatherCode,
                currentHourTemp
            ),
            timezone = response.timezone
        )
    }

    private fun toHourlyForecast(hourly: Hourly): Map<Int, List<HourlyForecast>> {
        return hourly.time.mapIndexed { index, time ->
            val currentTemp = hourly.temperature_2m[index]
            val weatherCode = hourly.weathercode[index]
            IndexedHourlyWeatherData(
                index = index,
                data = HourlyForecast(
                    currentDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    currentTemp = currentTemp.toInt(),
                    weatherCode = weatherCode
                )
            )
        }.groupBy {
            it.index / 24
        }.mapValues {
            it.value.map { hourlyWeatherData -> hourlyWeatherData.data }
        }
    }

    private fun toHourlyForecastList(response: WeatherResponse): List<HourlyForecast> {
        val hourlyForecast = toHourlyForecast(response.hourly)
        val todayHourly = hourlyForecast[0]
        val nextDayHourly: List<HourlyForecast>?
        val time = dateTimeHelper.currentLocalTime(response.timezone)
        return if (time > 0) {
            val resultHourly: ArrayList<HourlyForecast> = ArrayList()
            nextDayHourly = hourlyForecast[1]?.dropLast(24 - time)
            val todayHourlyWithDropElements = todayHourly?.drop(time)
            Stream.of(todayHourlyWithDropElements, nextDayHourly)
                .forEach { item -> resultHourly.addAll(item as Collection<HourlyForecast>) }
            resultHourly
        } else {
            todayHourly as ArrayList<HourlyForecast>
        }
    }

    private fun toDailyForecast(daily: Daily): Map<Int, List<DailyForecast>> {
        return List(daily.date.size) { index ->
            val maxTemperature = daily.temperature_2m_max[index].toInt()
            val minTemperature = daily.temperature_2m_min[index].toInt()
            val dayOfTheWeek = dateTimeHelper.run {
                daily.date.weekDays()[index]
            }
            val weatherCode = daily.weatherCode[index]
            IndexedDailyWeatherData(
                index = index,
                data = DailyForecast(
                    weatherCode = weatherCode,
                    maxTemperature = maxTemperature,
                    minTemperature = minTemperature,
                    dayOfTheWeek = dayOfTheWeek
                )
            )
        }.groupBy {
            it.index / 7
        }.mapValues {
            it.value.map { dailyWeatherData -> dailyWeatherData.data }
        }
    }


    private data class IndexedHourlyWeatherData(
        val index: Int,
        val data: HourlyForecast
    )

    private data class IndexedDailyWeatherData(
        val index: Int,
        val data: DailyForecast
    )
}