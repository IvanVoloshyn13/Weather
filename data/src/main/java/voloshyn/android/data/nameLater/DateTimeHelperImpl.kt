package voloshyn.android.data.nameLater

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import voloshyn.android.data.R
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class DateTimeHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DateTimeHelper {
    override fun LocalTime.toHour(): String {
        val time = this.toString()
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedDate = inputFormat.parse(time)!!
        return outputFormat.format(parsedDate)
    }

    override fun Milliseconds.toFormatHour(): String {
        val minutes = this / 60000
        return when (minutes.toInt()) {
            0 -> context.getString(R.string.last_update_less)
            in 1..59 -> context.getString(
                R.string.last_update_minutes, minutes,
                if (minutes > 1) "s" else ""
            )

            in 60..1440 -> {
                val hour = minutes / 60
                context.getString(
                    R.string.last_update_hours, hour,
                    if (hour > 1) "s" else ""
                )
            }

            else -> {
                val day = minutes / 1440
                if (day < 8) {
                    context.getString(
                        R.string.last_update_days, day,
                        if (day > 1) "s" else ""
                    )
                } else {
                    context.getString(
                        R.string.last_update_a_long_time_ago
                    )
                }
            }
        }
    }

    override fun lastUpdate(createdAt: Long): Long {
        return Clock.systemUTC().millis() - createdAt
    }

    override fun currentLocalTime(timezoneId: String): Int {
        val timeZone = TimeZone.getTimeZone(timezoneId)
        val zoneId = TimeZone.getTimeZone(timezoneId).toZoneId()
        val calendar = Calendar.getInstance(timeZone)
        val localTime = LocalTime.now(zoneId)
        return if (calendar.get(Calendar.MINUTE) < 30)
            localTime.hour else localTime.hour + 1
    }

    override fun ListOfTheWeekDate.weekDays(): List<String> {
        val daysList = ArrayList<String>()
        forEach { item ->
            val day = LocalDate.parse(item).dayOfWeek.name
            daysList.add(day)
        }
        return daysList
    }
}