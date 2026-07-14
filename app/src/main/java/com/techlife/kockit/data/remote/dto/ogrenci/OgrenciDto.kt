package com.techlife.kockit.data.remote.dto.ogrenci

data class OgrenciDto(
    val ogrenciId: Int,
    val kullaniciId: String,
    val sinifDuzeyId: Int? = null,
    val hazirlikSinavTurId: Int? = null,
    val hazirlikPuanTurId: Int? = null,
    val ogrenciDuzeyId: Int? = null,
    val ogrenciHedefId: Int? = null,
    val ogrenciHedef: String? = null,
    val okulTurId: Int? = null,
    val okulId: Int? = null,
    val notOrtalama: Double? = null,
    val okulAd: String? = null,
    val haftalikCalismaSure: Int? = null,
    val gunlukOturumSure: Int? = null,
    val genelTekrarGun: Int? = null,
    val gunlukParagrafSeans: Int? = null,
    val gunlukProblemSeans: Int? = null,
    val musaitOlmadigiGun: String? = null,
    val izinGunler: String? = null
)
