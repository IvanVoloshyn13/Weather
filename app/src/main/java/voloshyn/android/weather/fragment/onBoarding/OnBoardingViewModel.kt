package voloshyn.android.weather.fragment.onBoarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import voloshyn.android.domain.useCase.onBoarding.SavePushNotificationSettingsUseCase
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savePushNotificationSettingsUseCase: SavePushNotificationSettingsUseCase
) : ViewModel() {


}