package voloshyn.android.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.logging.Logger
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return Logger.getLogger(ERROR_LOGGER_NAME)
    }

    companion object {
        private const val ERROR_LOGGER_NAME = "LOG_ERROR"
    }
}