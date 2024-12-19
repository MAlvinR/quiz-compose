package co.malvinr.quiz_compose.network.di

import co.malvinr.quiz_compose.BuildConfig
import co.malvinr.quiz_compose.network.ApiNetwork
import co.malvinr.quiz_compose.network.ApiNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    }
            )
            .build()
    }


}

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkkModule {

    @Binds
    fun bindsApiNetworkDataSource(apiNetwork: ApiNetwork): ApiNetworkDataSource
}