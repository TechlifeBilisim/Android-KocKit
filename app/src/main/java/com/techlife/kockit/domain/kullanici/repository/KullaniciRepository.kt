package com.techlife.kockit.domain.kullanici.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.kullanici.model.KullaniciProfile
import com.techlife.kockit.domain.kullanici.model.UpdateKullaniciProfile

interface KullaniciRepository {
    suspend fun updateKullanici(profile: UpdateKullaniciProfile): ApiResult<KullaniciProfile>
}
