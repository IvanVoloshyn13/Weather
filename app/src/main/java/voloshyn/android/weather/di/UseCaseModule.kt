package voloshyn.android.weather.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import voloshyn.android.data.dataSource.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.OnBoardingRepository
import voloshyn.android.domain.repository.PlaceRepository
import voloshyn.android.domain.repository.PushNotificationRepository
import voloshyn.android.domain.repository.TimeForCurrentPlaceRepository
import voloshyn.android.domain.repository.WeatherAndImageRepository
import voloshyn.android.domain.useCase.addsearch.SavePlaceUseCase
import voloshyn.android.domain.useCase.addsearch.SearchPlaceByNameUseCase
import voloshyn.android.domain.useCase.mainActivity.GetOnBoardingStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.first.SavePushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.second.OnBoardingCompletedUseCase
import voloshyn.android.domain.useCase.onBoarding.second.SaveChosenPopularPlacesUseCase
import voloshyn.android.domain.useCase.weather.FetchWeatherAndImageDataUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase
import voloshyn.android.domain.useCase.weather.GetPlaceByIdUseCase
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetTimeForSelectedPlaceUseCase


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
    fun provideSavePopularPlacesUseCase(repository: PlaceRepository): SaveChosenPopularPlacesUseCase =
        SaveChosenPopularPlacesUseCase(repository)

    @Provides
    fun provideGetCurrentLocationUseCase(fusedLocationProvider: FusedLocationProvider): GetCurrentLocationUseCase =
        GetCurrentLocationUseCase(fusedLocationProvider)

    @Provides
    fun provideShowOnBoardingScreenUseCase(repository: OnBoardingRepository): GetOnBoardingStatusUseCase {
        return GetOnBoardingStatusUseCase(repository)
    }

    @Provides
    fun provideOnFinishedOnBoardingUseCase(repository: OnBoardingRepository): OnBoardingCompletedUseCase {
        return OnBoardingCompletedUseCase(repository)
    }


    @Provides
    fun provideGetTimeForLocationUseCase(timeRepository: TimeForCurrentPlaceRepository): GetTimeForSelectedPlaceUseCase {
        return GetTimeForSelectedPlaceUseCase(timeRepository)
    }


    @Provides
    fun provideSearchPlacesUseCase(repository: PlaceRepository): SearchPlaceByNameUseCase {
        return SearchPlaceByNameUseCase(repository)
    }

    @Provides
    fun provideSaveLocationUseCase(repository: PlaceRepository): SavePlaceUseCase {
        return SavePlaceUseCase(repository)
    }


    @Provides
    fun provideGetSavedPlacesUseCase(repository: PlaceRepository): GetSavedPlacesUseCase {
        return GetSavedPlacesUseCase(repository)
    }

    @Provides
    fun provideGetPlaceByIdUseCase(repository: PlaceRepository): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    @Provides
    fun provideFetchWeatherAndImageUseCase(repository: WeatherAndImageRepository): FetchWeatherAndImageDataUseCase {
        return FetchWeatherAndImageDataUseCase(repository)
    }

}

