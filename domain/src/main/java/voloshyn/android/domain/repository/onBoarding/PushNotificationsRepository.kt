package voloshyn.android.domain.repository.onBoarding

import voloshyn.android.domain.model.onBoarding.PushNotificationsSettings

interface PushNotificationsRepository {
    suspend fun savePushNotificationSettings(
        settings: PushNotificationsSettings,
        showNotifications: Boolean
    )

    suspend fun showNotification(): Boolean
    suspend fun getPushNotificationSettings(): PushNotificationsSettings
}