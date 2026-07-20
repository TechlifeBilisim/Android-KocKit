package com.techlife.kockit.domain.puansiralama.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaQuery
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaResult
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanQuery
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanResult
import com.techlife.kockit.domain.puansiralama.repository.PuanSiralamaRepository
import javax.inject.Inject

class GetSiralamaFromPuanUseCase @Inject constructor(
    private val repository: PuanSiralamaRepository
) {
    suspend operator fun invoke(query: PuandanSiralamaQuery): ApiResult<PuandanSiralamaResult> =
        repository.getSiralamaFromPuan(query)
}

class GetPuanFromSiralamaUseCase @Inject constructor(
    private val repository: PuanSiralamaRepository
) {
    suspend operator fun invoke(query: SiralamadanPuanQuery): ApiResult<SiralamadanPuanResult> =
        repository.getPuanFromSiralama(query)
}
