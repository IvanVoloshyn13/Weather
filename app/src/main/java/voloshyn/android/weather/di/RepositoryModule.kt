package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.repository.addSearch.SavePlaceRepositoryImpl
import voloshyn.android.data.repository.addSearch.SearchPlaceRepositoryImpl
import voloshyn.android.data.repository.mainActivity.OnBoardingImpl
import voloshyn.android.data.repository.onBoard.first.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.onBoard.second.OnFinishOnBoardingCompletedImpl
import voloshyn.android.data.repository.onBoard.second.PopularPlacesRepositoryImpl
import voloshyn.android.data.repository.weather.CurrentLocationWeatherRepositoryImpl
import voloshyn.android.data.repository.weather.GetPlaceByIdRepositoryImpl
import voloshyn.android.data.repository.weather.GetSavedPlacesRepositoryImpl
import voloshyn.android.data.repository.weather.LocationTimeRepositoryImpl
import voloshyn.android.data.repository.weather.UnsplashImageRepositoryImpl
import voloshyn.android.data.repository.weather.pager.SavedPlacesLocationRepositoryImpl
import voloshyn.android.data.repository.weather.pager.WeatherDataRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.addSearch.SavePlaceRepository
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import voloshyn.android.domain.repository.weather.CurrentLocationWeatherRepository
import voloshyn.android.domain.repository.weather.GetPlaceByIdRepository
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
import voloshyn.android.domain.repository.weather.LocationTimeRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.repository.weather.pager.SavedPlacesLocationRepository
import voloshyn.android.domain.repository.weather.pager.WeatherDataRepository

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
    fun bindCurrentLocationWeatherRepository(repository: CurrentLocationWeatherRepositoryImpl): CurrentLocationWeatherRepository

    @Binds
    fun bindLocationTimeRepository(repository: LocationTimeRepositoryImpl): LocationTimeRepository

    @Binds
    fun bindUnsplashImageRepository(unsplash: UnsplashImageRepositoryImpl): UnsplashImageRepository

    @Binds
    fun bindSearchLocationRepository(
        repository: SearchPlaceRepositoryImpl
    ): SearchPlaceRepository

    @Binds
    fun bindSaveLocationRepository(
        repository: SavePlaceRepositoryImpl
    ): SavePlaceRepository

    @Binds
    fun bindGetPlaceByIdRepository(
        repository: GetPlaceByIdRepositoryImpl
    ): GetPlaceByIdRepository

    @Binds
    fun bindGetSavedPlacesRepository(
        repository: GetSavedPlacesRepositoryImpl
    ): GetSavedPlacesRepository

    @Binds
    fun bindSavedLocationsRepository(repository: SavedPlacesLocationRepositoryImpl): SavedPlacesLocationRepository

    @Binds
    fun bindWeatherDataRepository(repository: WeatherDataRepositoryImpl): WeatherDataRepository
}