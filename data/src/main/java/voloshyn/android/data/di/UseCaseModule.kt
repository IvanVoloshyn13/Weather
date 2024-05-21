package voloshyn.android.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
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
 object UseCaseModule {

    //onBoarding

    @Provides
    @ViewModelScoped
    fun provideShowOnBoardingScreenUseCase(repository: OnBoardingRepository): GetOnBoardingStatusUseCase {
        return GetOnBoardingStatusUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideOnFinishedOnBoardingUseCase(repository: OnBoardingRepository): OnBoardingCompletedUseCase {
        return OnBoardingCompletedUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetShowNotificationStatus(
        pushNotificationRepository: PushNotificationRepository
    ): GetPushNotificationStatusUseCase =
        GetPushNotificationStatusUseCase(pushNotificationRepository)


    @Provides
    @ViewModelScoped
    fun provideSaveNotificationSettingsUseCase(
        pushNotificationRepository: PushNotificationRepository
    ): SavePushNotificationSettingsUseCase {
        return SavePushNotificationSettingsUseCase(pushNotificationRepository)
    }

    @Provides
    @ViewModelScoped
    fun providePushNotificationSettingsUseCase(
        pushNotificationRepository: PushNotificationRepository
    ): PushNotificationSettingsUseCase = PushNotificationSettingsUseCase(pushNotificationRepository)

    //UserLocation
    @Provides
    @ViewModelScoped
    fun provideGetCurrentLocationUseCase(fusedLocationProvider: FusedLocationProvider): GetCurrentLocationUseCase =
        GetCurrentLocationUseCase(fusedLocationProvider)

    //Time for current Place
    @Provides
    @ViewModelScoped
    fun provideGetTimeForLocationUseCase(timeRepository: TimeForCurrentPlaceRepository): GetTimeForSelectedPlaceUseCase {
        return GetTimeForSelectedPlaceUseCase(timeRepository)
    }

    //Place
    @Provides
    @ViewModelScoped
    fun provideInMemoryPlacesRepository(@ApplicationContext context: Context): InMemoryPopularPlacesRepositoryImpl =
        InMemoryPopularPlacesRepositoryImpl(context)

    @Provides
    @ViewModelScoped
    fun provideSavePopularPlacesUseCase(repository: PlaceRepository): SaveChosenPopularPlacesUseCase =
        SaveChosenPopularPlacesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSearchPlacesUseCase(repository: PlaceRepository): SearchPlaceByNameUseCase {
        return SearchPlaceByNameUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSavePlaceUseCase(repository: PlaceRepository): SavePlaceUseCase {
        return SavePlaceUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetSavedPlacesUseCase(repository: PlaceRepository): GetSavedPlacesUseCase {
        return GetSavedPlacesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetPlaceByIdUseCase(repository: PlaceRepository): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    //WeatherAndImage
    @Provides
    @ViewModelScoped
    fun provideFetchWeatherAndImageUseCase(repository: WeatherAndImageRepository): FetchWeatherAndImageDataUseCase {
        return FetchWeatherAndImageDataUseCase(repository)
    }

}

