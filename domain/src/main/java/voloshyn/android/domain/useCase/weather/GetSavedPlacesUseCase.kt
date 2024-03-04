package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.model.addSearchPlace.Place
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import voloshyn.android.domain.useCase.toResult


class GetSavedPlacesUseCase(private val repository: GetSavedPlacesRepository) {
    suspend fun invoke(): List<Place> {
      return  repository.getSavedCityList().toResult()
    }
}