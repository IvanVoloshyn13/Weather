package voloshyn.android.data.dataSource.local.datastorePreferences

import voloshyn.android.domain.Resource
import java.io.IOException

object DatastoreHelpers {
    suspend fun <T> dataToResource(block: suspend () -> Resource<T>): Resource<T> {
        return try {
            block()
        } catch (e: Exception) {
            if (e is IOException) {
                Resource.Error(e = IOException())
            } else {
                throw e
            }
        }
    }

    suspend fun saveToDataStore(block: suspend () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            if (e is IOException) {
                TODO()
            } else {
                throw e
            }
        }
    }
}