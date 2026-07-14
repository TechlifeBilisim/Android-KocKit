package com.techlife.kockit.domain.yo.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoFakulte

interface YoCatalogRepository {
    suspend fun getBilimler(): ApiResult<List<YoBilim>>
    suspend fun getFakulteler(): ApiResult<List<YoFakulte>>
}
