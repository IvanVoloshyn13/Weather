package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.repository.PushNotificationRepositoryImpl
import voloshyn.android.data.popularPlacesStorage.InMemoryPopularPlacesRepositoryImpl
import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository
import voloshyn.android.domain.useCase.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.first.SavePushNotificationSettingsUseCase

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

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
    fun provideInMemoryPlacesRepository(): InMemoryPopularPlacesRepositoryImpl =
        InMemoryPopularPlacesRepositoryImpl()


}

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun providePushNotificationRepository(pushNotificationRepository: PushNotificationRepositoryImpl): PushNotificationRepository


}