package voloshyn.android.http.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import voloshyn.android.network.http.interceptors.connectivity.ConnectivityInterceptor
import voloshyn.android.network.http.interceptors.emptyBody.EmptyBodyInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_MS = 30_000L

@Module
@InstallIn(SingletonComponent::class)
internal object HttpModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        connectivityInterceptor: ConnectivityInterceptor,
        emptyBodyInterceptor: EmptyBodyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(connectivityInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(emptyBodyInterceptor)
            .cache(Cache(File(context.cacheDir, "http_cache"), 50 * 1024 * 1024))
            .build()
    }
}