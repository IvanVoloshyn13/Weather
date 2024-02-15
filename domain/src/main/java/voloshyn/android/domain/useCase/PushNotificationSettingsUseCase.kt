package voloshyn.android.domain.useCase

import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository

class PushNotificationSettingsUseCase(private val pushNotificationRepository: PushNotificationRepository) {
    suspend fun invoke() = pushNotificationRepository.getPushNotificationSettings()
}