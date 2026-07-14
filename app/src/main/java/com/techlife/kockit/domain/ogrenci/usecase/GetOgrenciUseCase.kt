package com.techlife.kockit.domain.ogrenci.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.ogrenci.repository.OgrenciRepository
import javax.inject.Inject

class GetOgrenciUseCase @Inject constructor(
    private val ogrenciRepository: OgrenciRepository
) {
    suspend operator fun invoke(kullaniciId: String): ApiResult<Ogrenci> =
        ogrenciRepository.getOgrenci(kullaniciId)
}
