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
import voloshyn.android.domain.repository.weather.GetSavedPlacesRepository
<<<<<<< HEAD
import voloshyn.android.domain.repository.weather.LocationTimeRepository
import voloshyn.android.domain.repository.weather.UnsplashImageRepository
import voloshyn.android.domain.repository.weather.pager.SavedPlacesLocationRepository
import voloshyn.android.domain.repository.weather.pager.WeatherDataRepository
=======
import voloshyn.android.domain.repository.weather.TimeForCurrentPlaceRepository
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca
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
import voloshyn.android.domain.useCase.weather.GetSavedPlacesUseCase
<<<<<<< HEAD
import voloshyn.android.domain.useCase.weather.GetTimeForLocationUseCase
import voloshyn.android.domain.useCase.weather.pager.FetchMultipleWeatherDataUseCase
import voloshyn.android.domain.useCase.weather.pager.GetSavedPlacesLocationUseCase
=======
import voloshyn.android.domain.useCase.weather.GetTimeForSelectedPlaceUseCase
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca

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
    fun provideGetSavedPlacesUseCase(repository: GetSavedPlacesRepository): GetSavedPlacesUseCase {
        return GetSavedPlacesUseCase(repository)
    }

    @Provides
<<<<<<< HEAD
    fun provideGetSavedLocationsUseCase(repository: SavedPlacesLocationRepository): GetSavedPlacesLocationUseCase {
        return GetSavedPlacesLocationUseCase(repository)
    }

    @Provides
    fun provideWeatherDataUseCase(repository: WeatherDataRepository): FetchMultipleWeatherDataUseCase {
        return FetchMultipleWeatherDataUseCase(repository)
=======
    fun provideFetchWeatherAndImageUseCase(repository: FetchWeatherAndImageRepository):FetchWeatherAndImageDataUseCase{
        return FetchWeatherAndImageDataUseCase(repository)
>>>>>>> 2ade996e796081d5c8f5e2f97bdb45cae6cb57ca
    }
}

