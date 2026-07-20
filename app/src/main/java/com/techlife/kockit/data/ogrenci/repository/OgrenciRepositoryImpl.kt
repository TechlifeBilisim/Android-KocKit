package com.techlife.kockit.data.ogrenci.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.OgrenciRemoteDataSource
import com.techlife.kockit.domain.ogrenci.model.CalismaTakvimiUpdate
import com.techlife.kockit.domain.ogrenci.model.CreateOgrenciHedef
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.ogrenci.repository.OgrenciRepository
import javax.inject.Inject

class OgrenciRepositoryImpl @Inject constructor(
    private val ogrenciRemoteDataSource: OgrenciRemoteDataSource
) : OgrenciRepository {

    override suspend fun getOgrenci(kullaniciId: String): ApiResult<Ogrenci> =
        ogrenciRemoteDataSource.getOgrenci(kullaniciId)

    override suspend fun updateCalismaTakvimi(update: CalismaTakvimiUpdate): ApiResult<Ogrenci> =
        ogrenciRemoteDataSource.updateCalismaTakvimi(update)

    override suspend fun createOgrenciHedef(request: CreateOgrenciHedef): ApiResult<Unit> =
        ogrenciRemoteDataSource.createOgrenciHedef(request)
}
