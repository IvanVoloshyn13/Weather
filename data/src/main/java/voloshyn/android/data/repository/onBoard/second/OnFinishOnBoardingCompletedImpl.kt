package voloshyn.android.data.repository.onBoard.second

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import voloshyn.android.data.storage.datastorePreferences.DatastoreHelpers
import voloshyn.android.data.storage.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import javax.inject.Inject

class OnFinishOnBoardingCompletedImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoardingCompleted {
    override suspend fun saveOnBoardingStatus(completed: Boolean) {
        DatastoreHelpers.saveToDataStore {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.FINISH_ON_BOARDING] = completed
            }
        }
    }


}