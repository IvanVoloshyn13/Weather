package voloshyn.android.weather.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import voloshyn.android.data.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.addSearch.StorePlaceRepository
import voloshyn.android.domain.repository.addSearch.SearchPlaceRepository
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import voloshyn.android.domain.repository.weather.FetchWeatherAndImageRepository
import voloshyn.android.domain.repository.weather.SavedPlacesRepository
import voloshyn.android.domain.repository.weather.TimeForCurrentPlaceRepository
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
    fun provideGetTimeForLocationUseCase(timeRepository: TimeForCurrentPlaceRepository): GetTimeForSelectedPlaceUseCase {
        return GetTimeForSelectedPlaceUseCase(timeRepository)
    }


    @Provides
    fun provideSearchPlacesUseCase(searchPlace: SearchPlaceRepository): SearchPlaceByNameUseCase {
        return SearchPlaceByNameUseCase(searchPlace)
    }

    @Provides
    fun provideSaveLocationUseCase(saveLocation: StorePlaceRepository): SavePlaceUseCase {
        return SavePlaceUseCase(saveLocation)
    }


    @Provides
    fun provideGetSavedPlacesUseCase(repository: SavedPlacesRepository): GetSavedPlacesUseCase {
        return GetSavedPlacesUseCase(repository)
    }

    @Provides
    fun provideGetPlaceByIdUseCase(repository: SavedPlacesRepository): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    @Provides
    fun provideFetchWeatherAndImageUseCase(repository: FetchWeatherAndImageRepository): FetchWeatherAndImageDataUseCase {
        return FetchWeatherAndImageDataUseCase(repository)
    }

}

