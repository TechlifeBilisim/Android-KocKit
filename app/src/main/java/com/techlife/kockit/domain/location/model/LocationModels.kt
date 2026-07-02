package com.techlife.kockit.domain.location.model

data class Province(
    val id: Int,
    val name: String
)

data class District(
    val id: Int,
    val name: String,
    val provinceId: Int,
    val provinceName: String
)
