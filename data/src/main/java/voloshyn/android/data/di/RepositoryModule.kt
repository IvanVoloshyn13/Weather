package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepository
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepositoryImpl
import voloshyn.android.data.dataSource.remote.UnsplashRepository
import voloshyn.android.data.dataSource.remote.UnsplashRepositoryImpl
import voloshyn.android.data.dataSource.remote.WeatherRepository
import voloshyn.android.data.dataSource.remote.WeatherRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindUnsplashImageRepository(repository: UnsplashRepositoryImpl): UnsplashRepository

    @Binds
    fun bindWeatherRepository(repository:WeatherRepositoryImpl):WeatherRepository

    @Binds
    fun bindWeatherAndImageLocalDataSourceRepository(
        repository: WeatherAndImageLocalDataSourceRepositoryImpl
    ): WeatherAndImageLocalDataSourceRepository
}