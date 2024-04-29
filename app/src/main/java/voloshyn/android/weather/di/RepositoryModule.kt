package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.local.database.WeatherAndImageCacheRepositoryImpl
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.repository.addSearch.StorePlaceRepositoryImpl
import voloshyn.android.data.repository.addSearch.SearchPlaceRepositoryImpl
import voloshyn.android.data.repository.mainActivity.OnBoardingImpl
import voloshyn.android.data.repository.onBoard.first.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.onBoard.second.OnFinishOnBoardingCompletedImpl
import voloshyn.android.data.repository.onBoard.second.PopularPlacesRepositoryImpl
import voloshyn.android.data.repository.weather.GetSavedPlacesRepositoryImpl
import voloshyn.android.data.repository.weather.TimeForCurrentPlaceRepositoryImpl
import voloshyn.android.data.dataSource.remote.UnsplashImageRepositoryImpl
import voloshyn.android.data.dataSource.remote.WeatherRepositoryImpl
import voloshyn.android.data.repository.weather.FetchWeatherAndImageRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.domain.repository.cache.WeatherAndImageCacheRepository
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import voloshyn.android.domain.repository.weather.TimeForCurrentPlaceRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.repository.weather.WeatherRepository


@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindPushNotificationRepository(repository: PushNotificationRepositoryImpl): PushNotificationRepository

    @Binds
    fun bindPopularPlacesRepository(repository: PopularPlacesRepositoryImpl): PopularPlacesRepository

    @Binds
    fun bindFusedLocationProvider(fusedLocationProvider: FusedLocationProviderImpl): FusedLocationProvider

    @Binds
    fun bindOnBoardingRepository(onBoarding: OnBoardingImpl): OnBoarding

    @Binds
    fun bindOnFinishedOnBoarding(onFinished: OnFinishOnBoardingCompletedImpl): OnBoardingCompleted

    @Binds
    fun bindCurrentLocationWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindLocationTimeRepository(repository: TimeForCurrentPlaceRepositoryImpl): TimeForCurrentPlaceRepository

    @Binds
    fun bindUnsplashImageRepository(unsplash: UnsplashImageRepositoryImpl): UnsplashImageRepository

    @Binds
    fun bindSearchLocationRepository(
        repository: SearchPlaceRepositoryImpl
    ): SearchPlaceRepository

    @Binds
    fun bindSaveLocationRepository(
        repository: StorePlaceRepositoryImpl
    ): StorePlaceRepository



    @Binds
    fun bindGetSavedPlacesRepository(
        repository: GetSavedPlacesRepositoryImpl
    ): GetSavedPlacesRepository

    @Binds
    fun bindFetchWeatherAndImageRepository(
        repository: FetchWeatherAndImageRepositoryImpl
    ): FetchWeatherAndImageRepository

    @Binds
    fun bindWeatherAndImageCacheRepository(
        repository: WeatherAndImageCacheRepositoryImpl
    ): WeatherAndImageCacheRepository


}