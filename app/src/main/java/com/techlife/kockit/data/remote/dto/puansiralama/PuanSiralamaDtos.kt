package com.techlife.kockit.data.remote.dto.puansiralama

import com.squareup.moshi.Json

data class PuandanSiralamaDto(
    @Json(name = "sıralama") val siralama: Int
)

data class SiralamadanPuanDto(
    val puanYerlestirmeTurId: Int? = null,
    val puan: Double? = null,
    @Json(name = "sıralama") val siralama: Double? = null
)
