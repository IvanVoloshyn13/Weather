package voloshyn.android.domain.repository

import voloshyn.android.domain.model.onBoarding.PushNotificationSettings


interface PushNotificationRepository {
    suspend fun savePushNotificationSettings(
        settings: PushNotificationSettings,
        showNotifications: Boolean
    )

    suspend fun showNotification(): Boolean
    suspend fun getPushNotificationSettings(): PushNotificationSettings


}