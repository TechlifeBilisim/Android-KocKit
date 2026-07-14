package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.ogrenci.OgrenciDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OgrenciApiService {

    @GET("Api/Ogrenci")
    @ApiLog(ApiServices.OGRENCI_GET)
    suspend fun getOgrenci(
        @Query("kullaniciId") kullaniciId: String
    ): ApiEnvelopeDto<OgrenciDto>
}
