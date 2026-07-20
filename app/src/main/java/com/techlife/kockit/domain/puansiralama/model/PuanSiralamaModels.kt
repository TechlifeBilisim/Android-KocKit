package com.techlife.kockit.domain.puansiralama.model

data class PuandanSiralamaResult(
    val siralama: Int
)

data class SiralamadanPuanResult(
    val puanYerlestirmeTurId: Int? = null,
    val puan: Double? = null,
    val siralama: Double? = null
)

data class PuandanSiralamaQuery(
    val yil: Int,
    val puanTur: Int,
    val puan: Double,
    val okulPuan: Double
)

data class SiralamadanPuanQuery(
    val yil: Int,
    val puanTur: Int,
    val puanYerlestirmeTur: Int,
    val siralama: Double
)
