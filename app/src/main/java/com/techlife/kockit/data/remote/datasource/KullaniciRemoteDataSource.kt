package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.KullaniciApiService
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.mapper.toRequestDto
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.kullanici.model.KullaniciProfile
import com.techlife.kockit.domain.kullanici.model.UpdateKullaniciProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KullaniciRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val kullaniciApi: KullaniciApiService = apiServiceFactory.create()

    suspend fun updateKullanici(profile: UpdateKullaniciProfile): ApiResult<KullaniciProfile> =
        execute {
            kullaniciApi.updateKullanici(profile.toRequestDto()).requireData().toDomain()
        }
}
