package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository

class GetPushNotificationStatusUseCase(private val pushNotificationRepository: PushNotificationRepository) {
    suspend fun invoke() = pushNotificationRepository.showNotification()
}