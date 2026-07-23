package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.YoApiService
import com.techlife.kockit.data.remote.mapper.toYoBilimDomain
import com.techlife.kockit.data.remote.mapper.toYoBolumDomain
import com.techlife.kockit.data.remote.mapper.toYoFakulteDomain
import com.techlife.kockit.data.remote.mapper.toYoUniversiteDomain
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YoRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val yoApi: YoApiService = apiServiceFactory.create()

    suspend fun getBilimler(): ApiResult<List<YoBilim>> = execute {
        yoApi.getBilimler().requireData().toYoBilimDomain()
    }

    suspend fun getUniversiteler(unversiteTurId: Int? = null): ApiResult<List<YoUniversite>> = execute {
        yoApi.getUniversiteler(unversiteTurId).requireData().toYoUniversiteDomain()
    }

    suspend fun getFakulteler(yoUniversiteId: Int): ApiResult<List<YoFakulte>> = execute {
        yoApi.getFakulteler(yoUniversiteId).requireData().toYoFakulteDomain()
    }

    suspend fun getBolumler(
        yoBilimId: Int? = null,
        yoUniversiteId: Int? = null
    ): ApiResult<List<YoBolum>> = execute {
        yoApi.getBolumler(yoBilimId = yoBilimId, yoUniversiteId = yoUniversiteId)
            .requireData()
            .toYoBolumDomain()
    }
}
