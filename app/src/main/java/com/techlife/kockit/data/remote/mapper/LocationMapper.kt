package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.data.remote.dto.location.DistrictDto
import com.techlife.kockit.data.remote.dto.location.ProvinceDto
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province

fun ProvinceDto.toDomain(): Province = Province(
    id = ilId,
    name = ad
)

fun DistrictDto.toDomain(): District = District(
    id = ilceId,
    name = ad,
    provinceId = ilId,
    provinceName = ilAd
)

fun List<ProvinceDto>.toProvinceDomain(): List<Province> = map { it.toDomain() }

fun List<DistrictDto>.toDistrictDomain(): List<District> = map { it.toDomain() }
