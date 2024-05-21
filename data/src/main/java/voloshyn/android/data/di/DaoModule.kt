package voloshyn.android.data.di

import dagger.Module
import dagger.Provides

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.local.database.AppDatabase
import voloshyn.android.data.dataSource.local.database.dao.PlaceDao
import voloshyn.android.data.dataSource.local.database.dao.WeatherAndImageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun providePlaceDao(db: AppDatabase): PlaceDao {
        return db.placeDao()
    }

    @Provides
    @Singleton
    fun provideWeatherAndImageDao(db: AppDatabase): WeatherAndImageDao {
        return db.weatherAndImageDao()
    }
}