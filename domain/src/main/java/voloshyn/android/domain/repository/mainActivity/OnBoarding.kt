package voloshyn.android.domain.repository.mainActivity

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError

interface OnBoarding {
    suspend fun getOnBoardingStatus(): AppResult<Boolean,DataError.Locale>
}

