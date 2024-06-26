package voloshyn.android.network.http.utils

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import retrofit2.Response
import voloshyn.android.network.http.exceptions.ApiException
import voloshyn.android.network.http.interceptors.connectivity.NoConnectivityException
import java.io.IOException


suspend fun <T> executeApiCall(
    call: suspend () -> Response<T>,
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry,
    errorHandler: (Int, String?) -> Exception = ::defaultErrorHandler
): T {
        repeat(maxAttempts) { attempt ->
            try {
                return call().toResult(errorHandler)
            } catch (e: NoConnectivityException) {
                throw e
            } catch (e: Exception) {
                if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                    if (e is JsonDataException) {
                        Log.e("APICall", e.toString())
                    }
                    throw e
                }
            }
            val nextDelay = attempt * attempt * defaultDelay
            delay(nextDelay)
        }
    throw IllegalStateException("Unknown exception from executeWithRetry.")
}

private fun defaultShouldRetry(exception: Exception) = when (exception) {
    is ApiException -> exception.code == 429
    is IOException -> true
    else -> false
}

private fun defaultErrorHandler(code: Int, message: String?) = ApiException(code, message)


private fun <T> Response<T>.toResult(errorHandler: (Int, String?) -> Exception): T {
    return try {
        if (isSuccessful) {
            body()!!
        } else {
            val error = errorBody()?.let {
                val errorAdapter = Moshi.Builder().build().adapter(Error::class.java)
                errorAdapter.fromJson(it.source())
            }
            throw errorHandler(code(), error?.message)
        }
    } catch (_: Exception) {
        throw errorHandler(code(), null)

    }

}




