package voloshyn.android.data.mappers

import voloshyn.android.domain.Resource
import voloshyn.android.http.exceptions.ApiException

fun <T> ApiException.toResourceError(): Resource.Error<T> {

    return if (this.isClientError()) {
        Resource.Error(message = "The requested resource was not found")
    } else if (this.isServerError()) {
        Resource.Error(message = "An unexpected error occurred on the server")
    } else {
        Resource.Error(message = "Unknown exception")
    }
}