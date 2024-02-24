package voloshyn.android.data.repository.weather

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.domain.repository.weather.LocationTimeRepository
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class LocationTimeRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LocationTimeRepository {
    override fun getCurrentTimeForLocation(timeZoneId: String, updateTime: Boolean) = flow<String> {

        val timeZone = TimeZone.getTimeZone(timeZoneId).toZoneId()
        var time = LocalTime.now(timeZone)
        while (updateTime) {
            delay(1000)
            time = time.plusSeconds(1)
            val formattedTime = time.toHour()
            this.emit(formattedTime)
        }
    }.flowOn(dispatcher)


    private fun LocalTime.toHour(): String {
        val time = this.toString()
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedDate = inputFormat.parse(time)!!
        return outputFormat.format(parsedDate)

    }

}