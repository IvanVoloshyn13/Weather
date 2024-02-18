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
import voloshyn.android.data.repository.onBoard.PopularPlacesRepositoryImpl
import voloshyn.android.data.repository.onBoard.PushNotificationRepositoryImpl
import voloshyn.android.domain.location.FusedLocationProvider
import voloshyn.android.domain.repository.onBoarding.PopularPlacesRepository
import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository
import voloshyn.android.domain.useCase.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.first.SavePushNotificationSettingsUseCase
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


}

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun providePushNotificationRepository(pushNotificationRepository: PushNotificationRepositoryImpl): PushNotificationRepository

    @Binds
    fun providePopularPlacesRepository(popularPlacesRepository: PopularPlacesRepositoryImpl): PopularPlacesRepository

    @Binds
    fun provideFusedLocationProvider(fusedLocationProvider: FusedLocationProviderImpl): FusedLocationProvider

}