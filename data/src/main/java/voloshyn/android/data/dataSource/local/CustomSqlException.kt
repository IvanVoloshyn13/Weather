package voloshyn.android.data.dataSource.local

import android.database.SQLException

sealed class CustomSqlException : SQLException() {
    object NoSuchPlaceException : CustomSqlException()
}