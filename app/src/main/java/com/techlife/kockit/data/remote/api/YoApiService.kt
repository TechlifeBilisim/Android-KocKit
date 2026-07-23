package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.yo.YoBilimDto
import com.techlife.kockit.data.remote.dto.yo.YoBolumDto
import com.techlife.kockit.data.remote.dto.yo.YoFakulteDto
import com.techlife.kockit.data.remote.dto.yo.YoUniversiteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface YoApiService {

    @GET("Api/YOBilim")
    @ApiLog(ApiServices.YO_BILIM_LIST)
    suspend fun getBilimler(): ApiEnvelopeDto<List<YoBilimDto>>

    @GET("Api/YOUniversite")
    @ApiLog(ApiServices.YO_UNIVERSITE_LIST)
    suspend fun getUniversiteler(
        @Query("UnversiteTurId") unversiteTurId: Int? = null
    ): ApiEnvelopeDto<List<YoUniversiteDto>>

    @GET("Api/YOFakulte")
    @ApiLog(ApiServices.YO_FAKULTE_LIST)
    suspend fun getFakulteler(
        @Query("YOUniversiteId") yoUniversiteId: Int
    ): ApiEnvelopeDto<List<YoFakulteDto>>

    @GET("Api/YOBolum")
    @ApiLog(ApiServices.YO_BOLUM_LIST)
    suspend fun getBolumler(
        @Query("YOBilimId") yoBilimId: Int? = null,
        @Query("YOUniversiteId") yoUniversiteId: Int? = null
    ): ApiEnvelopeDto<List<YoBolumDto>>
}
