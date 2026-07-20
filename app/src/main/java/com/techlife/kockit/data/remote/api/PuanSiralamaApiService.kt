package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.puansiralama.PuandanSiralamaDto
import com.techlife.kockit.data.remote.dto.puansiralama.SiralamadanPuanDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PuanSiralamaApiService {

    @GET("Api/PuanSiralama/PuandanSiralama")
    @ApiLog(ApiServices.PUAN_SIRALAMA_PUANDAN)
    suspend fun getSiralamaFromPuan(
        @Query("Yıl") yil: Int,
        @Query("PuanTur") puanTur: Int,
        @Query("Puan") puan: Double,
        @Query("OkulPuan") okulPuan: Double
    ): ApiEnvelopeDto<PuandanSiralamaDto>

    @GET("Api/PuanSiralama/SiralamadanPuan")
    @ApiLog(ApiServices.PUAN_SIRALAMA_SIRALAMADAN)
    suspend fun getPuanFromSiralama(
        @Query("Yıl") yil: Int,
        @Query("PuanTur") puanTur: Int,
        @Query("PuanYerlestirmeTur") puanYerlestirmeTur: Int,
        @Query("Sıralama") siralama: Double
    ): ApiEnvelopeDto<SiralamadanPuanDto>
}
