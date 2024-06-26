package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.local.datastorePreferences.DatastoreHelpers.saveToDataStore
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys.EVERYDAY_WEATHER
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys.SHOW_NOTIFICATIONS
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys.TEMPERATURE_CHANGES
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys.UPCOMING_RAINFALL
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys.WEATHER_ALERT
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.model.onBoarding.toBoolean
import voloshyn.android.domain.model.onBoarding.toPushNotificationStatus
import voloshyn.android.domain.repository.PushNotificationRepository
import javax.inject.Inject

class PushNotificationRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PushNotificationRepository {

    override suspend fun savePushNotificationSettings(
        settings: PushNotificationSettings,
        showNotifications: Boolean
    ) {
        saveToDataStore {
            dataStore.edit { preferences ->
                with(settings) {
                    preferences[EVERYDAY_WEATHER] = everydayWeather.toBoolean()
                    preferences[UPCOMING_RAINFALL] = upcomingRainfall.toBoolean()
                    preferences[TEMPERATURE_CHANGES] = temperatureChanges.toBoolean()
                    preferences[WEATHER_ALERT] = weatherAlert.toBoolean()
                    preferences[SHOW_NOTIFICATIONS] = showNotifications
                }
            }
        }
    }


    //TODO() in the future, probably its will be the activity functional
    override suspend fun showNotification(): Boolean {
        val flow = dataStore.data.map { preferences ->
            preferences[SHOW_NOTIFICATIONS] ?: false
        }
        return flow.first()

    }

    //TODO() in the future, probably its will be the activity functional
    override suspend fun getPushNotificationSettings(): PushNotificationSettings {
        val notificationSettingsFlow = dataStore.data.map { preferences ->
            PushNotificationSettings(
                everydayWeather = (preferences[EVERYDAY_WEATHER]
                    ?: false).toPushNotificationStatus(),
                upcomingRainfall = (preferences[UPCOMING_RAINFALL]
                    ?: false).toPushNotificationStatus(),
                temperatureChanges = (preferences[TEMPERATURE_CHANGES]
                    ?: false).toPushNotificationStatus(),
                weatherAlert = (preferences[WEATHER_ALERT]
                    ?: false).toPushNotificationStatus(),
            )
        }
        return notificationSettingsFlow.first()
    }

}

