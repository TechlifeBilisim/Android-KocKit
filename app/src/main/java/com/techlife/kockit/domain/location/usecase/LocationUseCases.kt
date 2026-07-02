package com.techlife.kockit.domain.location.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province
import com.techlife.kockit.domain.location.repository.LocationRepository
import javax.inject.Inject

class GetProvincesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): ApiResult<List<Province>> = locationRepository.getProvinces()
}

class GetDistrictsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(provinceId: Int): ApiResult<List<District>> =
        locationRepository.getDistricts(provinceId)
}
