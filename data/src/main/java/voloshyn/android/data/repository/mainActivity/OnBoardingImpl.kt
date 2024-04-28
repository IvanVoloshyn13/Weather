package voloshyn.android.data.repository.mainActivity


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.local.datastorePreferences.DatastoreHelpers
import voloshyn.android.data.dataSource.local.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.Resource
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import javax.inject.Inject


class OnBoardingImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnBoarding {
    override suspend fun getOnBoardingStatus(): Resource<Boolean> {
        delay(500)
        return DatastoreHelpers.dataToResource {
            val flow = dataStore.data.map { preferences ->
                preferences[PreferencesKeys.FINISH_ON_BOARDING] ?: false
            }
            Resource.Success(data = flow.first())

        }
    }

}