package com.techlife.kockit.domain.yo.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.repository.YoCatalogRepository
import javax.inject.Inject

class GetYoBilimlerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(): ApiResult<List<YoBilim>> = yoCatalogRepository.getBilimler()
}

class GetYoFakultelerUseCase @Inject constructor(
    private val yoCatalogRepository: YoCatalogRepository
) {
    suspend operator fun invoke(): ApiResult<List<YoFakulte>> = yoCatalogRepository.getFakulteler()
}
