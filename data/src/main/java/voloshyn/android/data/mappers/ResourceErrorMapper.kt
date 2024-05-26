package voloshyn.android.data.mappers

import voloshyn.android.data.dataSource.local.CustomSqlException
import voloshyn.android.domain.appError.AppError
import voloshyn.android.domain.appError.DataError
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import java.io.IOException
import java.sql.SQLException

fun Exception.toDomainError(): DataError {
    return when (this) {
        is CustomSqlException.NoSuchPlaceException->DataError.Locale.DATA_NOT_FOUND
        is SQLException -> DataError.Locale.LOCAL_STORAGE_ERROR
        is NoConnectivityException->DataError.Network.N0_CONNECTION
        is IOException -> DataError.Network.UNKNOWN_ERROR
        is ApiException -> toResultError()
        else -> DataError.Network.UNKNOWN_ERROR
    }
}

fun ApiException.toResultError(): DataError.Network {
    return if (this.isClientError()) {
        DataError.Network.CLIENT_ERROR
    } else if (this.isServerError()) {
        DataError.Network.SERVER_ERROR
    } else {
        DataError.Network.UNKNOWN_ERROR
    }
}

