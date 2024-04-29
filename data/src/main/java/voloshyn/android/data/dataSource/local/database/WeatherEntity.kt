package voloshyn.android.data.dataSource.local.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import voloshyn.android.domain.model.weather.DailyForecast
import voloshyn.android.domain.model.weather.HourlyForecast
import voloshyn.android.domain.model.weather.CurrentForecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal const val WEATHER_TABLE_NAME = "weather"

@Entity(tableName = WEATHER_TABLE_NAME)
@TypeConverters(HourlyTypeConverter::class)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded val currentForecast: CurrentForecast,
    val hourlyForecast: List<HourlyForecast>? = null,
    val timezone: String? = "",
)

class HourlyTypeConverter {
    val gson = Gson()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME


    @TypeConverter
    fun fromListHourlyForecastToString(value: List<HourlyForecast>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromLocalDateTimeToString(value: LocalDateTime): String {
        return formatter.format(value)
    }

    @TypeConverter
    fun toHourlyList(value: String): List<HourlyForecast> {
        val data = gson.fromGsonType<List<HourlyForecast>>(value)
        return data
    }

}

@ProvidedTypeConverter
class DailyTypeConverter {
    val gson = Gson()

    @TypeConverter
    fun fromDailyForecastListToString(value: List<DailyForecast>): String {
        return gson.toJson(value)
    }
}

inline fun <reified T> Gson.fromGsonType(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

