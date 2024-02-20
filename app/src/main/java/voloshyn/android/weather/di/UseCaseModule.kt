package voloshyn.android.weather.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.location.FusedLocationProviderImpl
import voloshyn.android.data.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.data.repository.mainActivity.OnBoardingImpl
import voloshyn.android.data.repository.onBoard.first.PushNotificationRepositoryImpl
import voloshyn.android.data.repository.onBoard.second.OnFinishOnBoardingCompletedImpl
import voloshyn.android.data.repository.onBoard.second.PopularPlacesRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.mainActivity.OnBoarding
import voloshyn.android.domain.repository.onBoarding.first.PushNotificationRepository
import voloshyn.android.domain.repository.onBoarding.second.OnBoardingCompleted
import voloshyn.android.domain.repository.onBoarding.second.PopularPlacesRepository
import voloshyn.android.domain.useCase.mainActivity.GetOnBoardingStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.mainActivity.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.first.SavePushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.second.OnBoardingCompletedUseCase
import voloshyn.android.domain.useCase.onBoarding.second.SaveChosenPopularPlacesUseCase
import voloshyn.android.domain.useCase.weather.GetCurrentLocationUseCase

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


}

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

}