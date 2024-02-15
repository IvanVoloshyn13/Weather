package voloshyn.android.domain.useCase

import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository

class GetPushNotificationStatusUseCase(private val pushNotificationRepository: PushNotificationRepository) {
    suspend fun invoke() = pushNotificationRepository.showNotification()
}