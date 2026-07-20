package com.techlife.kockit.domain.kullanici.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.kullanici.model.KullaniciProfile
import com.techlife.kockit.domain.kullanici.model.UpdateKullaniciProfile
import com.techlife.kockit.domain.kullanici.repository.KullaniciRepository
import javax.inject.Inject

class UpdateKullaniciProfileUseCase @Inject constructor(
    private val kullaniciRepository: KullaniciRepository
) {
    suspend operator fun invoke(profile: UpdateKullaniciProfile): ApiResult<KullaniciProfile> =
        kullaniciRepository.updateKullanici(profile)
}
