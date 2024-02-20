package voloshyn.android.domain.useCase.onBoarding.second

import voloshyn.android.domain.model.onBoarding.PopularPlace
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository

class SaveChosenPopularPlacesUseCase(private val popularPlacesRepository: PopularPlacesRepository) {
    suspend fun invoke(places: Array<PopularPlace>) {
        if (places.isNotEmpty()) {
            popularPlacesRepository.savePlaces(places)
        }
    }
}