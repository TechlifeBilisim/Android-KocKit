package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.LocationApiService
import com.techlife.kockit.data.remote.mapper.toDistrictDomain
import com.techlife.kockit.data.remote.mapper.toProvinceDomain
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val locationApi: LocationApiService = apiServiceFactory.create()

    suspend fun getProvinces(): ApiResult<List<Province>> = execute {
        locationApi.getProvinces().requireData().toProvinceDomain()
    }

    suspend fun getDistricts(provinceId: Int): ApiResult<List<District>> = execute {
        locationApi.getDistricts(provinceId).requireData().toDistrictDomain()
    }
}
