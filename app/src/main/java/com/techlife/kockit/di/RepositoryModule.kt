package com.techlife.kockit.di

import com.techlife.kockit.data.auth.repository.AuthRepositoryImpl
import com.techlife.kockit.data.kullanici.repository.KullaniciRepositoryImpl
import com.techlife.kockit.data.lesson.repository.LessonRepositoryImpl
import com.techlife.kockit.data.location.repository.LocationRepositoryImpl
import com.techlife.kockit.data.ogrenci.repository.OgrenciRepositoryImpl
import com.techlife.kockit.data.onboarding.repository.OnboardingRepositoryImpl
import com.techlife.kockit.data.placement.repository.PlacementRepositoryImpl
import com.techlife.kockit.data.puansiralama.repository.PuanSiralamaRepositoryImpl
import com.techlife.kockit.data.yo.repository.YoCatalogRepositoryImpl
import com.techlife.kockit.domain.auth.repository.AuthRepository
import com.techlife.kockit.domain.kullanici.repository.KullaniciRepository
import com.techlife.kockit.domain.lesson.repository.LessonRepository
import com.techlife.kockit.domain.location.repository.LocationRepository
import com.techlife.kockit.domain.ogrenci.repository.OgrenciRepository
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import com.techlife.kockit.domain.placement.repository.PlacementRepository
import com.techlife.kockit.domain.puansiralama.repository.PuanSiralamaRepository
import com.techlife.kockit.domain.yo.repository.YoCatalogRepository
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

    @Binds
    @Singleton
    abstract fun bindPlacementRepository(impl: PlacementRepositoryImpl): PlacementRepository

    @Binds
    @Singleton
    abstract fun bindLessonRepository(impl: LessonRepositoryImpl): LessonRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindYoCatalogRepository(impl: YoCatalogRepositoryImpl): YoCatalogRepository

    @Binds
    @Singleton
    abstract fun bindOgrenciRepository(impl: OgrenciRepositoryImpl): OgrenciRepository

    @Binds
    @Singleton
    abstract fun bindKullaniciRepository(impl: KullaniciRepositoryImpl): KullaniciRepository

    @Binds
    @Singleton
    abstract fun bindPuanSiralamaRepository(impl: PuanSiralamaRepositoryImpl): PuanSiralamaRepository
}
