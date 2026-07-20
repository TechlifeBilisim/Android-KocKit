package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.PuanSiralamaApiService
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaQuery
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaResult
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanQuery
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PuanSiralamaRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val puanSiralamaApi: PuanSiralamaApiService = apiServiceFactory.create()

    suspend fun getSiralamaFromPuan(query: PuandanSiralamaQuery): ApiResult<PuandanSiralamaResult> =
        execute {
            puanSiralamaApi.getSiralamaFromPuan(
                yil = query.yil,
                puanTur = query.puanTur,
                puan = query.puan,
                okulPuan = query.okulPuan
            ).requireData().toDomain()
        }

    suspend fun getPuanFromSiralama(query: SiralamadanPuanQuery): ApiResult<SiralamadanPuanResult> =
        execute {
            puanSiralamaApi.getPuanFromSiralama(
                yil = query.yil,
                puanTur = query.puanTur,
                puanYerlestirmeTur = query.puanYerlestirmeTur,
                siralama = query.siralama
            ).requireData().toDomain()
        }
}
