package voloshyn.android.domain.useCase.onBoarding

import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository

class SavePushNotificationSettingsUseCase(private val pushNotificationRepository: PushNotificationRepository) {

    suspend fun invoke(
        pushNotificationSettings: PushNotificationSettings
    ) {
        val showNotifications = pushNotificationSettings.showNotifications
        pushNotificationRepository.savePushNotificationSettings(
            pushNotificationSettings,
            showNotifications
        )
    }
}