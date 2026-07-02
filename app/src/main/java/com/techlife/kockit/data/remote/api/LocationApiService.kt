package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.location.DistrictDto
import com.techlife.kockit.data.remote.dto.location.ProvinceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {

    @GET("api/il")
    @ApiLog(ApiServices.PROVINCE_LIST)
    suspend fun getProvinces(): ApiEnvelopeDto<List<ProvinceDto>>

    @GET("api/ilce")
    @ApiLog(ApiServices.DISTRICT_LIST)
    suspend fun getDistricts(
        @Query("IlId") provinceId: Int
    ): ApiEnvelopeDto<List<DistrictDto>>
}
