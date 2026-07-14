package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.yo.YoBilimDto
import com.techlife.kockit.data.remote.dto.yo.YoFakulteDto
import retrofit2.http.GET

interface YoApiService {

    @GET("Api/YOBilim")
    @ApiLog(ApiServices.YO_BILIM_LIST)
    suspend fun getBilimler(): ApiEnvelopeDto<List<YoBilimDto>>

    @GET("Api/YOFakulte")
    @ApiLog(ApiServices.YO_FAKULTE_LIST)
    suspend fun getFakulteler(): ApiEnvelopeDto<List<YoFakulteDto>>
}
