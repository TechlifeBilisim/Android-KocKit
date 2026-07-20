package com.techlife.kockit.data.puansiralama.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.PuanSiralamaRemoteDataSource
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaQuery
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaResult
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanQuery
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanResult
import com.techlife.kockit.domain.puansiralama.repository.PuanSiralamaRepository
import javax.inject.Inject

class PuanSiralamaRepositoryImpl @Inject constructor(
    private val remoteDataSource: PuanSiralamaRemoteDataSource
) : PuanSiralamaRepository {

    override suspend fun getSiralamaFromPuan(
        query: PuandanSiralamaQuery
    ): ApiResult<PuandanSiralamaResult> = remoteDataSource.getSiralamaFromPuan(query)

    override suspend fun getPuanFromSiralama(
        query: SiralamadanPuanQuery
    ): ApiResult<SiralamadanPuanResult> = remoteDataSource.getPuanFromSiralama(query)
}
