package voloshyn.android.data.dataSource.local.datastorePreferences

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val EVERYDAY_WEATHER = booleanPreferencesKey("everydayWeather")
    val UPCOMING_RAINFALL = booleanPreferencesKey("upcomingRainfall")
    val TEMPERATURE_CHANGES = booleanPreferencesKey("temperatureChanges")
    val WEATHER_ALERT = booleanPreferencesKey("weatherAlert")
    val SHOW_NOTIFICATIONS = booleanPreferencesKey("showNotifications")
    val FINISH_ON_BOARDING = booleanPreferencesKey("finish_onBoarding")
}