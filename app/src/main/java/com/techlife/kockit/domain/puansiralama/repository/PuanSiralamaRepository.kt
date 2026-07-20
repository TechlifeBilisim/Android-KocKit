package com.techlife.kockit.domain.puansiralama.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaQuery
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaResult
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanQuery
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanResult

interface PuanSiralamaRepository {
    suspend fun getSiralamaFromPuan(query: PuandanSiralamaQuery): ApiResult<PuandanSiralamaResult>
    suspend fun getPuanFromSiralama(query: SiralamadanPuanQuery): ApiResult<SiralamadanPuanResult>
}
