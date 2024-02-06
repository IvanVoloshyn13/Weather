package voloshyn.android.domain.model.onBoarding

data class PushNotificationsSettings(
    val everydayWeather: PushNotificationStatus,
    val upcomingRainfall: PushNotificationStatus,
    val temperatureChanges: PushNotificationStatus,
    val weatherAlert: PushNotificationStatus
) {

    val showNotifications:Boolean
        get() = everydayWeather.toBoolean() || upcomingRainfall.toBoolean() || temperatureChanges.toBoolean() || weatherAlert.toBoolean()

}

enum class PushNotificationStatus {
    ENABLED, DISABLED
}

fun Boolean.toPushNotificationStatus(): PushNotificationStatus {
    return when (this) {
        true -> PushNotificationStatus.ENABLED
        false -> PushNotificationStatus.DISABLED
    }
}

fun PushNotificationStatus.toBoolean(): Boolean {
    return when (this) {
        PushNotificationStatus.ENABLED -> true
        PushNotificationStatus.DISABLED -> false
    }
}


//class PushNotificationsTest {
//    private val notifyEverydayWeather = false
//    private val notifyUpcomingRainfall = false
//    private val notifyWeatherAlert = false
//    private val notifyTemperatureChanges = true
//
//
//    fun showNotification() {
//        // Given
//        val pushNotifications = PushNotifications(
//            notifyEverydayWeather,
//            notifyUpcomingRainfall,
//            notifyTemperatureChanges,
//            notifyWeatherAlert
//        )
//
//        val showNotifications = pushNotifications.showNotifications
//        println(showNotifications)
//
//
//    }
//}




