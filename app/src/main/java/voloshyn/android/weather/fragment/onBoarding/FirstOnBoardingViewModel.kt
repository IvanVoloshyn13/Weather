package voloshyn.android.weather.fragment.onBoarding

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.model.onBoarding.toPushNotificationStatus
import voloshyn.android.domain.useCase.onBoarding.SavePushNotificationSettingsUseCase
import javax.inject.Inject


@HiltViewModel
class FirstOnBoardingViewModel @Inject constructor(
    private val savePushNotificationSettingsUseCase: SavePushNotificationSettingsUseCase
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("EXCEPTION_HANDLER", throwable.toString())
        Log.d("EXCEPTION_HANDLER", throwable.message.toString())
    }

    private val viewModelScope = CoroutineScope(coroutineExceptionHandler)

    suspend fun savePushNotificationSettings(
        everydayWeather: Boolean,
        upcomingRainfall: Boolean,
        temperatureChanges: Boolean,
        weatherAlert: Boolean
    ) {
        val pushSettings = PushNotificationSettings(
            everydayWeather.toPushNotificationStatus(),
            upcomingRainfall.toPushNotificationStatus(),
            temperatureChanges.toPushNotificationStatus(),
            weatherAlert.toPushNotificationStatus()
        )
        viewModelScope.launch {
            savePushNotificationSettingsUseCase.invoke(pushNotificationSettings = pushSettings)
        }

    }


}