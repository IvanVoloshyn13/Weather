package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepository
import voloshyn.android.data.dataSource.local.database.WeatherAndImageLocalDataSourceRepositoryImpl
import voloshyn.android.data.dataSource.popularPlacesStorage.PopularPlaceData
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceHandler
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceHandlerImpl
import voloshyn.android.data.dataSource.remote.UnsplashRepository
import voloshyn.android.data.dataSource.remote.UnsplashRepositoryImpl
import voloshyn.android.data.dataSource.remote.WeatherRepository
import voloshyn.android.data.dataSource.remote.WeatherRepositoryImpl
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.repository.OnBoardingRepositoryImpl
import voloshyn.android.data.repository.PlaceRepositoryImpl
import voloshyn.android.data.repository.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.TimeForCurrentPlaceRepositoryImpl
import voloshyn.android.data.repository.WeatherAndImageRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.OnBoardingRepository
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.PushNotificationRepository
import voloshyn.android.domain.repository.TimeForCurrentPlaceRepository
import voloshyn.android.domain.repository.WeatherAndImageRepository
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    //OnBoarding

    @Binds
    @PlacesMultiChoice
    @ViewModelScoped
    fun bindMultiChoiceHandler(
        multiChoice: MultiChoiceHandlerImpl
    ): MultiChoiceHandler<PopularPlaceData>
    @Binds
    @ViewModelScoped
    fun bindOnBoardingRepository(repository: OnBoardingRepositoryImpl): OnBoardingRepository

    @Binds
    @ViewModelScoped
    fun bindPushNotificationRepository(repository: PushNotificationRepositoryImpl): PushNotificationRepository

    //WeatherAndImage
    @Binds
    @ViewModelScoped
    fun bindUnsplashImageRepository(repository: UnsplashRepositoryImpl): UnsplashRepository

    @Binds
    @ViewModelScoped
    fun bindWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @ViewModelScoped
    fun bindWeatherAndImageRepository(repository: WeatherAndImageRepositoryImpl): WeatherAndImageRepository

    @Binds
    @ViewModelScoped
    fun bindWeatherAndImageLocalDataSourceRepository(
        repository: WeatherAndImageLocalDataSourceRepositoryImpl
    ): WeatherAndImageLocalDataSourceRepository

    //Place
    @Binds
    @ViewModelScoped
    fun bindPlacesRepository(repository: PlaceRepositoryImpl): PlaceRepository


    //Location and Time
    @Binds
    @ViewModelScoped
    fun bindFusedLocationProvider(fusedLocationProvider: FusedLocationProviderImpl): FusedLocationProvider
    @Binds
    @ViewModelScoped
    fun bindLocationTimeRepository(repository: TimeForCurrentPlaceRepositoryImpl): TimeForCurrentPlaceRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlacesMultiChoice