package com.techlife.kockit.domain.yo.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite

interface YoCatalogRepository {
    suspend fun getBilimler(): ApiResult<List<YoBilim>>
    suspend fun getUniversiteler(unversiteTurId: Int? = null): ApiResult<List<YoUniversite>>
    suspend fun getFakulteler(yoUniversiteId: Int): ApiResult<List<YoFakulte>>
    suspend fun getBolumler(
        yoBilimId: Int? = null,
        yoUniversiteId: Int? = null
    ): ApiResult<List<YoBolum>>
}
