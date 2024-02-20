package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.Resource
import voloshyn.android.domain.repository.mainActivity.OnBoarding

class GetOnBoardingStatusUseCase(private val onBoarding: OnBoarding) {
    suspend fun invoke(): Resource<Boolean> = onBoarding.getOnBoardingStatus()
}