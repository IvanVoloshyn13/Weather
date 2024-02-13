package voloshyn.android.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.domain.Resource
import voloshyn.android.domain.model.onBoarding.PushNotificationSettings
import voloshyn.android.domain.model.onBoarding.toBoolean
import voloshyn.android.domain.model.onBoarding.toPushNotificationStatus
import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository
import java.io.IOException
import javax.inject.Inject

class PushNotificationRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PushNotificationRepository {

    override suspend fun savePushNotificationSettings(
        settings: PushNotificationSettings,
        showNotifications: Boolean
    ) {
        dataStore.edit { preferences ->
            with(settings) {
                preferences[PreferencesKeys.EVERYDAY_WEATHER] = everydayWeather.toBoolean()
                preferences[PreferencesKeys.UPCOMING_RAINFALL] = upcomingRainfall.toBoolean()
                preferences[PreferencesKeys.TEMPERATURE_CHANGES] =
                    temperatureChanges.toBoolean()
                preferences[PreferencesKeys.WEATHER_ALERT] = weatherAlert.toBoolean()
                preferences[PreferencesKeys.SHOW_NOTIFICATIONS] = showNotifications
            }
        }
    }

    override suspend fun showNotification(): Resource<Boolean> {
        return dataToResource {
            val flow = dataStore.data.map { preferences ->
                preferences[PreferencesKeys.SHOW_NOTIFICATIONS] ?: false
            }
            Resource.Success(data = flow.first())
        }
    }

    override suspend fun getPushNotificationSettings(): Resource<PushNotificationSettings> {
        return dataToResource {
            val notificationSettingsFlow = dataStore.data.map { preferences ->
                PushNotificationSettings(
                    everydayWeather = (preferences[PreferencesKeys.EVERYDAY_WEATHER]
                        ?: false).toPushNotificationStatus(),
                    upcomingRainfall = (preferences[PreferencesKeys.UPCOMING_RAINFALL]
                        ?: false).toPushNotificationStatus(),
                    temperatureChanges = (preferences[PreferencesKeys.TEMPERATURE_CHANGES]
                        ?: false).toPushNotificationStatus(),
                    weatherAlert = (preferences[PreferencesKeys.WEATHER_ALERT]
                        ?: false).toPushNotificationStatus(),
                )
            }
            Resource.Success(notificationSettingsFlow.first())
        }
    }


    companion object {
        object PreferencesKeys {
            val EVERYDAY_WEATHER = booleanPreferencesKey("everydayWeather")
            val UPCOMING_RAINFALL = booleanPreferencesKey("upcomingRainfall")
            val TEMPERATURE_CHANGES = booleanPreferencesKey("temperatureChanges")
            val WEATHER_ALERT = booleanPreferencesKey("weatherAlert")
            val SHOW_NOTIFICATIONS = booleanPreferencesKey("showNotifications")
        }
    }


    private suspend fun <T> dataToResource(someFun: suspend () -> Resource<T>): Resource<T> {
        return try {
            someFun()
        } catch (e: Exception) {
            if (e is IOException) {
                Resource.Error(message = e.message)
            } else {
                throw e
            }
        }

    }
}

