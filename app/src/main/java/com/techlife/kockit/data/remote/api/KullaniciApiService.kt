package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.kullanici.KullaniciProfilDto
import com.techlife.kockit.data.remote.dto.kullanici.UpdateKullaniciRequestDto
import retrofit2.http.Body
import retrofit2.http.PUT

interface KullaniciApiService {

    @PUT("Api/Kullanici")
    @ApiLog(ApiServices.KULLANICI_UPDATE)
    suspend fun updateKullanici(
        @Body request: UpdateKullaniciRequestDto
    ): ApiEnvelopeDto<KullaniciProfilDto>
}
