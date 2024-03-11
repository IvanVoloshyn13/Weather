package voloshyn.android.weather.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import voloshyn.android.data.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
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
import voloshyn.android.domain.useCase.addsearch.SavePlaceUseCase
import voloshyn.android.domain.useCase.addsearch.SearchPlaceByNameUseCase
import voloshyn.android.domain.useCase.mainActivity.GetOnBoardingStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.first.SavePushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.second.OnBoardingCompletedUseCase
import voloshyn.android.domain.useCase.onBoarding.second.SaveChosenPopularPlacesUseCase
import voloshyn.android.domain.useCase.weather.FetchUnsplashImageByCityNameUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherForCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetPlaceByIdUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForLocationUseCase

@Module
@InstallIn(ViewModelComponent::class)
internal object UseCaseModule {

    @Provides
    fun provideGetShowNotificationStatus(
        pushNotificationRepository: PushNotificationRepository
    ): GetPushNotificationStatusUseCase =
        GetPushNotificationStatusUseCase(pushNotificationRepository)


    @Provides
    fun provideSaveNotificationSettingsUseCase(
        pushNotificationRepository: PushNotificationRepository
    ): SavePushNotificationSettingsUseCase {
        return SavePushNotificationSettingsUseCase(pushNotificationRepository)
    }

    @Provides
    fun providePushNotificationSettingsUseCase(
        pushNotificationRepository: PushNotificationRepository
    ): PushNotificationSettingsUseCase = PushNotificationSettingsUseCase(pushNotificationRepository)

    @Provides
    fun provideInMemoryPlacesRepository(@ApplicationContext context: Context): InMemoryPopularPlacesRepositoryImpl =
        InMemoryPopularPlacesRepositoryImpl(context)

    @Provides
    fun provideSavePopularPlacesUseCase(popularPlacesRepository: PopularPlacesRepository): SaveChosenPopularPlacesUseCase =
        SaveChosenPopularPlacesUseCase(popularPlacesRepository)

    @Provides
    fun provideGetCurrentLocationUseCase(fusedLocationProvider: FusedLocationProvider): GetCurrentLocationUseCase =
        GetCurrentLocationUseCase(fusedLocationProvider)

    @Provides
    fun provideShowOnBoardingScreenUseCase(onBoarding: OnBoarding): GetOnBoardingStatusUseCase {
        return GetOnBoardingStatusUseCase(onBoarding)
    }

    @Provides
    fun provideOnFinishedOnBoardingUseCase(onFinished: OnBoardingCompleted): OnBoardingCompletedUseCase {
        return OnBoardingCompletedUseCase(onFinished)
    }

    @Provides
    fun provideFetchCurrentLocationWeatherUseCase(weatherRepository: CurrentLocationWeatherRepository): FetchWeatherForCurrentLocationUseCase {
        return FetchWeatherForCurrentLocationUseCase(weatherRepository)
    }

    @Provides
    fun provideGetTimeForLocationUseCase(timeRepository: LocationTimeRepository): GetTimeForLocationUseCase {
        return GetTimeForLocationUseCase(timeRepository)
    }

    @Provides
    fun provideFetchUnsplashImage(unsplash: UnsplashImageRepository): FetchUnsplashImageByCityNameUseCase {
        return FetchUnsplashImageByCityNameUseCase(unsplash)
    }

    @Provides
    fun provideSearchPlacesUseCase(searchPlace: SearchPlaceRepository): SearchPlaceByNameUseCase {
        return SearchPlaceByNameUseCase(searchPlace)
    }

    @Provides
    fun provideSaveLocationUseCase(saveLocation: SavePlaceRepository): SavePlaceUseCase {
        return SavePlaceUseCase(saveLocation)
    }

    @Provides
    fun provideGetPlaceByUseCase(repository: GetPlaceByIdRepository): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    @Provides
    fun provideGetSavedPlacesUseCase(repository: GetSavedPlacesRepository): GetSavedPlacesUseCase {
        return GetSavedPlacesUseCase(repository)
    }
}

