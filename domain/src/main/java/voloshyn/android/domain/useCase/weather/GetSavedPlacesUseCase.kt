package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository


class GetSavedPlacesUseCase(private val repository: GetSavedPlacesRepository) {
    suspend fun invoke() = repository.getSavedCityList()
}