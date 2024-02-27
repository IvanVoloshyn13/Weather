package voloshyn.android.domain.useCase.weather

import voloshyn.android.domain.repository.weather.GetPlaceByIdRepository
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.addSearchPlace.Place

class GetPlaceByIdUseCase(private val repository: GetPlaceByIdRepository) {

    suspend fun invoke(id: Int): Resource<Place> {
        return repository.getPlaceById(id)
    }
}