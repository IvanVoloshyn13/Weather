package voloshyn.android.domain.repository.weather

import kotlinx.coroutines.flow.Flow

interface TimeForCurrentPlaceRepository {

    /**
     * Observe and show time for current [Place] user choose
     * */
    fun observeTime(timeZoneId: String, updateTime: Boolean): Flow<String>

}