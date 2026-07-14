package com.techlife.kockit.domain.yo.model

data class YoBilim(
    val id: Int,
    val name: String
)

data class YoUniversite(
    val id: Int,
    val name: String
)

data class YoFakulte(
    val id: Int,
    val universityId: Int? = null,
    val name: String
)

data class YoBolum(
    val id: Int,
    val bilimId: Int? = null,
    val name: String
)
