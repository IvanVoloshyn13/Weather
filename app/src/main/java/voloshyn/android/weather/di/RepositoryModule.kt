package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepositoryImpl
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.repository.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.TimeForCurrentPlaceRepositoryImpl
import voloshyn.android.data.repository.WeatherAndImageRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepository
import voloshyn.android.data.repository.OnBoardingRepositoryImpl
import voloshyn.android.data.repository.PlaceRepositoryImpl
import voloshyn.android.domain.repository.OnBoardingRepository
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.PushNotificationRepository
import voloshyn.android.domain.repository.WeatherAndImageRepository
import voloshyn.android.domain.repository.TimeForCurrentPlaceRepository


@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindFusedLocationProvider(fusedLocationProvider: FusedLocationProviderImpl): FusedLocationProvider

    @Binds
    fun bindPushNotificationRepository(repository: PushNotificationRepositoryImpl): PushNotificationRepository

    @Binds
    fun bindPlacesRepository(repository: PlaceRepositoryImpl): PlaceRepository

    @Binds
    fun bindOnBoardingRepository(repository: OnBoardingRepositoryImpl): OnBoardingRepository

    @Binds
    fun bindWeatherAndImageRepository(repository: WeatherAndImageRepositoryImpl): WeatherAndImageRepository

    @Binds
    fun bindLocationTimeRepository(repository: TimeForCurrentPlaceRepositoryImpl): TimeForCurrentPlaceRepository




}