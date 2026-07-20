package com.techlife.kockit.domain.ogrenci.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.ogrenci.model.CalismaTakvimiUpdate
import com.techlife.kockit.domain.ogrenci.model.CreateOgrenciHedef
import com.techlife.kockit.domain.ogrenci.model.Ogrenci

interface OgrenciRepository {
    suspend fun getOgrenci(kullaniciId: String): ApiResult<Ogrenci>
    suspend fun updateCalismaTakvimi(update: CalismaTakvimiUpdate): ApiResult<Ogrenci>
    suspend fun createOgrenciHedef(request: CreateOgrenciHedef): ApiResult<Unit>
}
