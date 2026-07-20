package com.techlife.kockit.domain.ogrenci.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.ogrenci.model.CreateOgrenciHedef
import com.techlife.kockit.domain.ogrenci.repository.OgrenciRepository
import javax.inject.Inject

class CreateOgrenciHedefUseCase @Inject constructor(
    private val ogrenciRepository: OgrenciRepository
) {
    suspend operator fun invoke(request: CreateOgrenciHedef): ApiResult<Unit> =
        ogrenciRepository.createOgrenciHedef(request)
}
