package voloshyn.android.domain.useCase.mainActivity

import voloshyn.android.domain.repository.PushNotificationRepository

class GetPushNotificationStatusUseCase(private val repository: PushNotificationRepository) {
    suspend fun invoke() = repository.showNotification()
}