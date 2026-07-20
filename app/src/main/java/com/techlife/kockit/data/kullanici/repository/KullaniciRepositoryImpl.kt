package com.techlife.kockit.data.kullanici.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.KullaniciRemoteDataSource
import com.techlife.kockit.domain.kullanici.model.KullaniciProfile
import com.techlife.kockit.domain.kullanici.model.UpdateKullaniciProfile
import com.techlife.kockit.domain.kullanici.repository.KullaniciRepository
import javax.inject.Inject

class KullaniciRepositoryImpl @Inject constructor(
    private val kullaniciRemoteDataSource: KullaniciRemoteDataSource
) : KullaniciRepository {

    override suspend fun updateKullanici(profile: UpdateKullaniciProfile): ApiResult<KullaniciProfile> =
        kullaniciRemoteDataSource.updateKullanici(profile)
}
