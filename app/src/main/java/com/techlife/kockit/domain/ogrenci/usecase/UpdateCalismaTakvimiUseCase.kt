package com.techlife.kockit.domain.ogrenci.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.ogrenci.model.CalismaTakvimiUpdate
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.ogrenci.repository.OgrenciRepository
import javax.inject.Inject

class UpdateCalismaTakvimiUseCase @Inject constructor(
    private val ogrenciRepository: OgrenciRepository
) {
    suspend operator fun invoke(update: CalismaTakvimiUpdate): ApiResult<Ogrenci> =
        ogrenciRepository.updateCalismaTakvimi(update)
}
