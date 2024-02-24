package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.repository.mainActivity.OnBoardingImpl
import voloshyn.android.data.repository.onBoard.first.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.onBoard.second.OnFinishOnBoardingCompletedImpl
import voloshyn.android.data.repository.onBoard.second.PopularPlacesRepositoryImpl
import voloshyn.android.data.repository.weather.CurrentLocationWeatherRepositoryImpl
import voloshyn.android.data.repository.weather.LocationTimeRepositoryImpl
import voloshyn.android.data.repository.weather.UnsplashImageRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository
import voloshyn.android.domain.repository.weather.LocationTimeRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindPushNotificationRepository(pushNotificationRepository: PushNotificationRepositoryImpl): PushNotificationRepository

    @Binds
    fun bindPopularPlacesRepository(popularPlacesRepository: PopularPlacesRepositoryImpl): PopularPlacesRepository

    @Binds
    fun bindFusedLocationProvider(fusedLocationProvider: FusedLocationProviderImpl): FusedLocationProvider

    @Binds
    fun bindOnBoardingRepository(onBoarding: OnBoardingImpl): OnBoarding

    @Binds
    fun bindOnFinishedOnBoarding(onFinished: OnFinishOnBoardingCompletedImpl): OnBoardingCompleted

    @Binds
    fun bindCurrentLocationWeatherRepository(weatherRepository: CurrentLocationWeatherRepositoryImpl): CurrentLocationWeatherRepository

    @Binds
    fun bindLocationTimeRepository(timeRepository: LocationTimeRepositoryImpl): LocationTimeRepository

    @Binds
    fun bindUnsplashImageRepository(unsplash: UnsplashImageRepositoryImpl): UnsplashImageRepository
}