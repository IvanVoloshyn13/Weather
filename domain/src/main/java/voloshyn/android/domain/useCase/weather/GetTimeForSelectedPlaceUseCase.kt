package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.repository.weather.TimeForCurrentPlaceRepository

class GetTimeForSelectedPlaceUseCase(private val timeRepo: TimeForCurrentPlaceRepository) {

     fun invoke(timeZoneId: String, updateTime: Boolean): Flow<String> {
        return timeRepo.observeTime(timeZoneId, updateTime)
    }

}