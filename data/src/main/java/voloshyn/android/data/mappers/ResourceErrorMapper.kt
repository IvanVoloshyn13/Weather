package voloshyn.android.data.mappers

import voloshyn.android.domain.appError.DataError
import voloshyn.android.network.http.exceptions.ApiException


fun ApiException.toResultError(): DataError.Network {
    return if (this.isClientError()) {
        DataError.Network.CLIENT_ERROR
    } else if (this.isServerError()) {
        DataError.Network.SERVER_ERROR
    } else {
        DataError.Network.UNKNOWN_ERROR
    }
}



