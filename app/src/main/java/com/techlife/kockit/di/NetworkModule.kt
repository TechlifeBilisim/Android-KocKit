package com.techlife.kockit.di

import com.techlife.kockit.BuildConfig
import com.techlife.kockit.core.network.auth.SessionTokenProvider
import com.techlife.kockit.core.network.auth.TokenProvider
import com.techlife.kockit.core.network.interceptor.AuthTokenInterceptor
import com.techlife.kockit.core.network.interceptor.KocKitLoggingInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideIsApiLoggingEnabled(): Boolean = BuildConfig.ENABLE_API_LOGGING

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideTokenProvider(
        sessionTokenProvider: SessionTokenProvider
    ): TokenProvider = sessionTokenProvider

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authTokenInterceptor: AuthTokenInterceptor,
        loggingInterceptor: KocKitLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(NetworkTimeouts.CONNECT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NetworkTimeouts.READ_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NetworkTimeouts.WRITE_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(authTokenInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

private object NetworkTimeouts {
    const val CONNECT_SECONDS = 30L
    const val READ_SECONDS = 30L
    const val WRITE_SECONDS = 30L
}
