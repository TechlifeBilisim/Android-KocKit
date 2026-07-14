package com.techlife.kockit.data.yo.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.YoRemoteDataSource
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite
import com.techlife.kockit.domain.yo.repository.YoCatalogRepository
import javax.inject.Inject

class YoCatalogRepositoryImpl @Inject constructor(
    private val yoRemoteDataSource: YoRemoteDataSource
) : YoCatalogRepository {

    override suspend fun getBilimler(): ApiResult<List<YoBilim>> =
        yoRemoteDataSource.getBilimler()

    override suspend fun getUniversiteler(): ApiResult<List<YoUniversite>> =
        yoRemoteDataSource.getUniversiteler()

    override suspend fun getFakulteler(yoUniversiteId: Int): ApiResult<List<YoFakulte>> =
        yoRemoteDataSource.getFakulteler(yoUniversiteId)

    override suspend fun getBolumler(
        yoBilimId: Int?,
        yoUniversiteId: Int?
    ): ApiResult<List<YoBolum>> =
        yoRemoteDataSource.getBolumler(yoBilimId = yoBilimId, yoUniversiteId = yoUniversiteId)
}
