package voloshyn.android.network.retrofit.di

import android.util.Log
import com.slack.eithernet.ApiResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import voloshyn.android.network.retrofit.apiServices.ApiSearchCityService
import voloshyn.android.network.retrofit.apiServices.ApiWeatherService
import voloshyn.android.network.retrofit.utils.OpenMeteo
import voloshyn.android.network.retrofit.utils.OpenMeteoApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object OpenMeteoModule {


    @Provides
    @Singleton
    @OpenMeteoApi
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        val callFactory = Call.Factory { request -> okHttpClient.newCall(request) }
        return Retrofit.Builder()
            .callFactory(callFactory)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)


    }

    @Provides
    @Singleton
    fun provideWeatherService(@OpenMeteoApi retrofit: Retrofit.Builder): ApiWeatherService =
        retrofit.baseUrl(OpenMeteo.BASE_URL).build().create(ApiWeatherService::class.java)

    @Provides
    @Singleton
    fun provideSearchCityService(@OpenMeteoApi retrofit: Retrofit.Builder): ApiSearchCityService =
        retrofit.baseUrl(OpenMeteo.SEARCH_URL).build().create(ApiSearchCityService::class.java)


}