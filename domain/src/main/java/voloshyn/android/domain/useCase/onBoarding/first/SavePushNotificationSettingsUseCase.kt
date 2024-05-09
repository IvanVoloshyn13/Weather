package voloshyn.android.domain.useCase.onBoarding.first

import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.repository.PushNotificationRepository

class SavePushNotificationSettingsUseCase(private val repository: PushNotificationRepository) {

    suspend fun invoke(
        pushNotificationSettings: PushNotificationSettings
    ) {
        val showNotifications = pushNotificationSettings.showNotifications
        repository.savePushNotificationSettings(
            pushNotificationSettings,
            showNotifications
        )
    }
}