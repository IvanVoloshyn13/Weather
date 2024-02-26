package voloshyn.android.network.http.exceptions

class ApiException(val code: Int, message: String?) : Exception(message) {

    fun isClientError(): Boolean = code in 400..499

    fun isServerError(): Boolean = code >= 500
}

sealed class ApiExceptions : Exception()
object ClientException : ApiExceptions()
object ServerException : ApiExceptions()
object UnknownException : ApiExceptions()