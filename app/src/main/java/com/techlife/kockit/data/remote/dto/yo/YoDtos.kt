package com.techlife.kockit.data.remote.dto.yo

data class YoBilimDto(
    val yoBilimId: Int,
    val ad: String
)

data class YoUniversiteDto(
    val yoUniversiteId: Int,
    val ad: String
)

data class YoFakulteDto(
    val yoFakulteId: Int,
    val yoUniversiteId: Int? = null,
    val ad: String
)

data class YoBolumDto(
    val yoBolumId: Int,
    val yoBilimId: Int? = null,
    val ad: String
)
