package voloshyn.android.domain.repository.onBoarding.first

import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings


interface PushNotificationRepository {
    suspend fun savePushNotificationSettings(
        settings: PushNotificationSettings,
        showNotifications: Boolean
    )

    suspend fun showNotification(): Resource<Boolean>
    suspend fun getPushNotificationSettings(): Resource<PushNotificationSettings>


}