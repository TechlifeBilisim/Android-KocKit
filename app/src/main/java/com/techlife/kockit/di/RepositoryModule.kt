package com.techlife.kockit.di

import com.techlife.kockit.data.auth.repository.AuthRepositoryImpl
import com.techlife.kockit.data.onboarding.repository.OnboardingRepositoryImpl
import com.techlife.kockit.domain.auth.repository.AuthRepository
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository
}
