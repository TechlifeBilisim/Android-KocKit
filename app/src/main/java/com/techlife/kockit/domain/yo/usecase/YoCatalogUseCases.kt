package com.techlife.kockit.domain.yo.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite
import com.techlife.kockit.domain.yo.repository.YoCatalogRepository
import javax.inject.Inject

class GetYoBilimlerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(): ApiResult<List<YoBilim>> = yoCatalogRepository.getBilimler()
}

class GetYoUniversitelerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(): ApiResult<List<YoUniversite>> =
        yoCatalogRepository.getUniversiteler()
}

class GetYoFakultelerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(yoUniversiteId: Int): ApiResult<List<YoFakulte>> =
        yoCatalogRepository.getFakulteler(yoUniversiteId)
}

class GetYoBolumlerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(
        yoBilimId: Int? = null,
        yoUniversiteId: Int? = null
    ): ApiResult<List<YoBolum>> =
        yoCatalogRepository.getBolumler(yoBilimId = yoBilimId, yoUniversiteId = yoUniversiteId)
}
