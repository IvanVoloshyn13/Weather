package voloshyn.android.data.mappers

import voloshyn.android.domain.error.AppResult
import voloshyn.android.domain.error.DataError
import voloshyn.android.network.http.exceptions.ApiException

fun <D>ApiException.toResultError(): AppResult.Error<D,DataError.Network> {
    return if (this.isClientError()) {
        AppResult.Error(error = DataError.Network.CLIENT_ERROR)
    } else if (this.isServerError()) {
        AppResult.Error(error = DataError.Network.CLIENT_ERROR)
    } else {
        AppResult.Error(error = DataError.Network.UNKNOWN_ERROR)
    }
}

