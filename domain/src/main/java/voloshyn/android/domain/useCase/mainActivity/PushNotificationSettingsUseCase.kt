package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository

class PushNotificationSettingsUseCase(private val pushNotificationRepository: PushNotificationRepository) {
    suspend fun invoke() = pushNotificationRepository.getPushNotificationSettings()
}