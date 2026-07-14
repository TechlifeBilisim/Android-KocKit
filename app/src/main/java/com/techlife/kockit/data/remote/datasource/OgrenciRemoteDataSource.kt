package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.OgrenciApiService
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OgrenciRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val ogrenciApi: OgrenciApiService = apiServiceFactory.create()

    suspend fun getOgrenci(kullaniciId: String): ApiResult<Ogrenci> = execute {
        ogrenciApi.getOgrenci(kullaniciId).requireData().toDomain()
    }
}
