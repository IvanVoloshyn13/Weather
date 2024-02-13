package voloshyn.android.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.repository.PushNotificationRepositoryImpl
import voloshyn.android.domain.repository.onBoarding.PushNotificationRepository
import voloshyn.android.domain.useCase.onBoarding.GetPushNotificationStatusUseCase
import voloshyn.android.domain.useCase.onBoarding.PushNotificationSettingsUseCase
import voloshyn.android.domain.useCase.onBoarding.SavePushNotificationSettingsUseCase

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
        return  SavePushNotificationSettingsUseCase(pushNotificationRepository)
    }



    @Provides
    fun providePushNotificationSettingsUseCase(
        pushNotificationRepository: PushNotificationRepository
    ): PushNotificationSettingsUseCase = PushNotificationSettingsUseCase(pushNotificationRepository)


}

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun providePushNotificationRepository(pushNotificationRepository: PushNotificationRepositoryImpl): PushNotificationRepository
}