package voloshyn.android.network.http.utils

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>( val e: Exception) : ApiResult<T>()
}