package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.local.datastorePreferences.DatastoreHelpers
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.appError.AppResult
import voloshyn.android.domain.appError.DataError
import voloshyn.android.domain.repository.OnBoardingRepository
import javax.inject.Inject

class OnBoardingRepositoryImpl  @Inject constructor(
    private val dataStore: DataStore<Preferences>
):OnBoardingRepository {
    override suspend fun storeOnBoardingStatus(completed: Boolean) {
        DatastoreHelpers.saveToDataStore {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.FINISH_ON_BOARDING] = completed
            }
        }
    }

    override suspend fun getOnBoardingStatus(): AppResult<Boolean, DataError.Locale> {
        return try {
            delay(500)
            val flow = dataStore.data.map { preferences ->
                preferences[PreferencesKeys.FINISH_ON_BOARDING] ?: false
            }
            AppResult.Success(data = flow.first())
        } catch (e: IOException) {
            AppResult.Error(error = DataError.Locale.LOCAL_STORAGE_ERROR)
        }
    }
}