package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.repository.OnBoardingRepository

class GetOnBoardingStatusUseCase(private val repository: OnBoardingRepository) {
    suspend fun invoke(): AppResult<Boolean, DataError.Locale> = repository.getOnBoardingStatus()
}