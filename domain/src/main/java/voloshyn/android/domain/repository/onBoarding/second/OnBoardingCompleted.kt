package voloshyn.android.domain.repository.onBoarding.second

interface OnBoardingCompleted {

    suspend fun saveOnBoardingStatus(completed: Boolean)
}