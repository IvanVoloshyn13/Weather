package voloshyn.android.data.nameLater

import java.time.LocalTime

typealias Milliseconds = Long
typealias ListOfTheWeekDate = List<String>

interface DateTimeHelper {

    fun LocalTime.toHour(): String

    fun Milliseconds.toFormatHour(): String

    fun lastUpdate(createdAt:Long): Long

    fun currentLocalTime(timezoneId: String): Int

    fun ListOfTheWeekDate.weekDays(): List<String>

}