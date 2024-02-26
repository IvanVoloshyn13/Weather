package voloshyn.android.data.mappers

import voloshyn.android.domain.Resource
import voloshyn.android.domain.customError.ClientError
import voloshyn.android.domain.customError.CustomError
import voloshyn.android.domain.customError.ServerError
import voloshyn.android.domain.customError.UnknownError
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.exceptions.ApiExceptions
import voloshyn.android.network.http.exceptions.ClientException
import voloshyn.android.network.http.exceptions.ServerException
import voloshyn.android.network.http.exceptions.UnknownException

fun <T> ApiException.toResourceError(): Resource.Error<T> {
    return if (this.isClientError()) {
        Resource.Error(e = ClientException.toCustomError())
    } else if (this.isServerError()) {
        Resource.Error(e = ServerException.toCustomError())
    } else {
        Resource.Error(e = UnknownException.toCustomError())
    }
}

fun ApiExceptions.toCustomError(): CustomError {
    return when (this) {
        is ClientException -> ClientError()
        is ServerException -> ServerError()
        is UnknownException -> UnknownError()

    }
}