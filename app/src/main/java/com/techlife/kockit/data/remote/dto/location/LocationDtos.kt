package com.techlife.kockit.data.remote.dto.location

data class ProvinceDto(
    val ilId: Int,
    val ad: String
)

data class DistrictDto(
    val ilceId: Int,
    val ad: String,
    val ilId: Int,
    val ilAd: String
)
