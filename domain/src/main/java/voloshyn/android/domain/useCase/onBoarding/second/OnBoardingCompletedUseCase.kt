package voloshyn.android.domain.useCase.onBoarding.second

import voloshyn.android.domain.repository.OnBoardingRepository

class OnBoardingCompletedUseCase(private val repository: OnBoardingRepository) {

    suspend fun invoke(isFinished: Boolean) = repository.storeOnBoardingStatus(isFinished)

}