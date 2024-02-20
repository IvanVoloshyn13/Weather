package voloshyn.android.domain.repository.mainActivity

import voloshyn.android.domain.Resource

interface OnBoarding {
    suspend fun getOnBoardingStatus(): Resource<Boolean>
}

