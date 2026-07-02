package com.techlife.kockit.data.location.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.LocationRemoteDataSource
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province
import com.techlife.kockit.domain.location.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationRemoteDataSource: LocationRemoteDataSource
) : LocationRepository {

    override suspend fun getProvinces(): ApiResult<List<Province>> =
        locationRemoteDataSource.getProvinces()

    override suspend fun getDistricts(provinceId: Int): ApiResult<List<District>> =
        locationRemoteDataSource.getDistricts(provinceId)
}
