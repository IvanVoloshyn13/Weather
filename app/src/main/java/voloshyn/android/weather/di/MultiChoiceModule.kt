package voloshyn.android.weather.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import voloshyn.android.data.dataSource.popularPlacesStorage.PopularPlaceData
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceHandler
import voloshyn.android.data.dataSource.popularPlacesStorage.multichoice.MultiChoiceHandlerImpl
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlacesMultiChoice

@Module
@InstallIn(ViewModelComponent::class)
class MultiChoiceModule {

    @Provides
    @PlacesMultiChoice
    fun provideMultiChoiceHandler(): MultiChoiceHandler<PopularPlaceData> {
        return MultiChoiceHandlerImpl()
    }

}
