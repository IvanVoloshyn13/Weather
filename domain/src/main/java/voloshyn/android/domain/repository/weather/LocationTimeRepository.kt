package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow
interface LocationTimeRepository {
    fun getCurrentTimeForLocation(timeZoneId: String, updateTime: Boolean): Flow<String>

}