package com.techlife.kockit.domain.location.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province

interface LocationRepository {
    suspend fun getProvinces(): ApiResult<List<Province>>
    suspend fun getDistricts(provinceId: Int): ApiResult<List<District>>
}
