package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.domain.repository.mainActivity.OnBoarding

class GetOnBoardingStatusUseCase(private val onBoarding: OnBoarding) {
    suspend fun invoke(): AppResult<Boolean, DataError.Locale> = onBoarding.getOnBoardingStatus()
}