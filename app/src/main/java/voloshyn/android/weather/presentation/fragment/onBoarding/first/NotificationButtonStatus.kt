package voloshyn.android.weather.presentation.fragment.onBoarding.first

data class NotificationButtonStatus(
    val isEverydayWeatherNotificationEnabled: Boolean,
    val isUpcomingRainfallNotificationEnabled: Boolean,
    val isTemperatureChangesNotificationEnabled: Boolean,
    val isWeatherAlertNotificationEnabled: Boolean
) {
    val status: Boolean
        get() = (isEverydayWeatherNotificationEnabled ||
                isTemperatureChangesNotificationEnabled ||
                isUpcomingRainfallNotificationEnabled ||
                isWeatherAlertNotificationEnabled)
}
