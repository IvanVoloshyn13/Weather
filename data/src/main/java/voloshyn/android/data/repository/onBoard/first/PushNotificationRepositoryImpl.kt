package voloshyn.android.data.repository.onBoard.first

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.storage.datastorePreferences.DatastoreHelpers.dataToResource
import voloshyn.android.data.storage.datastorePreferences.DatastoreHelpers.saveToDataStore
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.EVERYDAY_WEATHER
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.FINISH_ON_BOARDING
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.SHOW_NOTIFICATIONS
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.TEMPERATURE_CHANGES
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.UPCOMING_RAINFALL
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys.WEATHER_ALERT
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.model.onBoarding.toBoolean
import voloshyn.android.domain.model.onBoarding.toPushNotificationStatus
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
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
    override suspend fun showNotification(): Resource<Boolean> {
        return dataToResource {
            val flow = dataStore.data.map { preferences ->
                preferences[SHOW_NOTIFICATIONS] ?: false
            }
            Resource.Success(data = flow.first())
        }
    }

    //TODO() in the future, probably its will be the activity functional
    override suspend fun getPushNotificationSettings(): Resource<PushNotificationSettings> {
        return dataToResource {
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
            Resource.Success(notificationSettingsFlow.first())
        }
    }



}

