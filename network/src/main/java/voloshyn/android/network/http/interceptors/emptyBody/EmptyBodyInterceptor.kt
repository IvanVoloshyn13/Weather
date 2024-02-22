package voloshyn.android.http.interceptors.emptyBody

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject

internal class EmptyBodyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful || (response.code != HttpURLConnection.HTTP_NO_CONTENT && response.code != HttpURLConnection.HTTP_RESET)) {
            return response
        }
        return response.newBuilder()
            .code(200)
            .apply {
                val body = response.body
                if (body == null || body.contentLength() == 0L) {
                    body(EMPTY_BODY)
                }
            }.build()
    }

    companion object {
        val EMPTY_BODY = "".toResponseBody("text/plain".toMediaType())
    }
}