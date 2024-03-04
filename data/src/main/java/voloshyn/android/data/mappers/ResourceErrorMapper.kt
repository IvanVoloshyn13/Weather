package voloshyn.android.data.mappers

import android.content.Context
import voloshyn.android.data.R
import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.ClientError
import voloshyn.android.domain.customError.ServerError
import voloshyn.android.domain.customError.UnknownError
import voloshyn.android.network.http.exceptions.ApiException

fun <T> ApiException.toResourceError(context: Context): Resource.Error<T> {
    return if (this.isClientError()) {
        Resource.Error(e = ClientError(message = context.getString(R.string.client_error)))
    } else if (this.isServerError()) {
        Resource.Error(e = ServerError(message = context.getString(R.string.server_error)))
    } else {
        Resource.Error(e = UnknownError(message = context.getString(R.string.unknown_error)))
    }
}

