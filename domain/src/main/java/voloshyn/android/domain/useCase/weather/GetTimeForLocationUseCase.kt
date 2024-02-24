package voloshyn.android.domain.useCase.weather

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.repository.weather.LocationTimeRepository

class GetTimeForLocationUseCase(private val timeRepo: LocationTimeRepository) {

     fun invoke(timeZoneId: String, updateTime: Boolean): Flow<String> {
        return timeRepo.getCurrentTimeForLocation(timeZoneId, updateTime)
    }

}