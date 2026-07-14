package com.techlife.kockit.domain.ogrenci.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.ogrenci.model.Ogrenci

interface OgrenciRepository {
    suspend fun getOgrenci(kullaniciId: String): ApiResult<Ogrenci>
}
