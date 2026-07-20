package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.common.EmptyDataDto
import com.techlife.kockit.data.remote.dto.ogrenci.CreateOgrenciHedefRequestDto
import com.techlife.kockit.data.remote.dto.ogrenci.OgrenciDto
import com.techlife.kockit.data.remote.dto.ogrenci.UpdateCalismaTakvimiRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OgrenciApiService {

    @GET("Api/Ogrenci")
    @ApiLog(ApiServices.OGRENCI_GET)
    suspend fun getOgrenci(
        @Query("kullaniciId") kullaniciId: String
    ): ApiEnvelopeDto<OgrenciDto>

    @PUT("Api/Ogrenci/CalismaTakvimi")
    @ApiLog(ApiServices.OGRENCI_CALISMA_TAKVIMI_UPDATE)
    suspend fun updateCalismaTakvimi(
        @Body request: UpdateCalismaTakvimiRequestDto
    ): ApiEnvelopeDto<OgrenciDto>

    @POST("Api/OgrenciHedef")
    @ApiLog(ApiServices.OGRENCI_HEDEF_CREATE)
    suspend fun createOgrenciHedef(
        @Body request: CreateOgrenciHedefRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>
}
