package voloshyn.android.domain.repository

import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError

interface OnBoardingRepository {
    /**  Store onBoarding status in DataStore preferences
     * @param [completed]*/
    suspend fun storeOnBoardingStatus(completed: Boolean)

    /**  Return onBoarding status from DataStore preferences */
    suspend fun getOnBoardingStatus(): AppResult<Boolean, DataError.Locale>
}