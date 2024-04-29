package voloshyn.android.data.dataSource.local.datastorePreferences


import java.io.IOException

object DatastoreHelpers {

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