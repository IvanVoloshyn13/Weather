package voloshyn.android.domain.repository.mainActivity

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError

interface OnBoarding {
    suspend fun getOnBoardingStatus(): AppResult<Boolean,DataError.Locale>
}

