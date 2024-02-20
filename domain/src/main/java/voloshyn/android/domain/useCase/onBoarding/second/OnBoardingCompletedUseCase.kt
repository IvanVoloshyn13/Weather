package voloshyn.android.domain.useCase.onBoarding.second

import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted

class OnBoardingCompletedUseCase(private val completed: OnBoardingCompleted) {

    suspend fun invoke(isFinished: Boolean) = completed.saveOnBoardingStatus(isFinished)

}