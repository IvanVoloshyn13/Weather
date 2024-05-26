package voloshyn.android.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import voloshyn.android.data.di.IoDispatcher
import voloshyn.android.data.nameLater.DateTimeHelper
import voloshyn.android.domain.repository.TimeForCurrentPlaceRepository
import java.time.LocalTime
import java.util.TimeZone
import javax.inject.Inject

class TimeForCurrentPlaceRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TimeForCurrentPlaceRepository {
   @Inject
   lateinit var dateTimeHelper: DateTimeHelper


    override fun observeTime(timeZoneId: String, updateTime: Boolean) = flow<String> {
        val timeZone = TimeZone.getTimeZone(timeZoneId).toZoneId()
        var time = LocalTime.now(timeZone)
        while (updateTime) {
            delay(1000)
            time = time.plusSeconds(1)
            val formattedTime = dateTimeHelper.run {
                time.toHour()
            }
            this.emit(formattedTime)
        }
    }.flowOn(dispatcher)


}