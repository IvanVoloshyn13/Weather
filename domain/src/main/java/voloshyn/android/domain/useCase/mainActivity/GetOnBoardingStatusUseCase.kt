package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.repository.mainActivity.OnBoarding

class GetOnBoardingStatusUseCase(private val onBoarding: OnBoarding) {
    suspend fun invoke(): AppResult<Boolean, DataError.Locale> = onBoarding.getOnBoardingStatus()
}