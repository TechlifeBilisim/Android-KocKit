package com.techlife.kockit.data.remote.dto.ogrenci

import com.squareup.moshi.Json

data class OgrenciKullaniciDto(
    val kullaniciId: String,
    val ad: String? = null,
    val soyad: String? = null,
    val cepTelefon: String? = null,
    val loginTypeId: Int? = null,
    val rumuz: String? = null,
    val eposta: String? = null,
    val cinsiyetId: Int? = null,
    val resim: String? = null,
    val kvkkOnay: Boolean? = null,
    val uygulamaTemaId: Int? = null,
    val epostaOnay: Boolean? = null,
    val telefonOnay: Boolean? = null,
    val ilId: Int? = null,
    val ilceId: Int? = null,
    val kullaniciTurId: Int? = null
)

data class OgrenciDto(
    val ogrenciId: Int,
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
    val kullanici: OgrenciKullaniciDto? = null,
    val haftalikCalismaSure: Int? = null,
    val gunlukOturumSure: Int? = null,
    val genelTekrarGun: Int? = null,
    val gunlukParagrafSeans: Int? = null,
    val gunlukProblemSeans: Int? = null,
    val musaitOlmadigiGun: Int? = null,
    val izinGunler: List<Int>? = null
)

data class UpdateCalismaTakvimiRequestDto(
    val haftalikCalismaSure: Int,
    val gunlukOturumSure: Int,
    val genelTekrarGun: Int,
    val gunlukParagrafSeans: Int,
    val gunlukProblemSeans: Int,
    val musaitOlmadigiGun: Int,
    val izinGunler: List<Int>
)

data class OgrenciHedefTercihDto(
    val yoUniversiteId: Int,
    val yoBolumId: Int
)

data class CreateOgrenciHedefRequestDto(
    val ogrenciId: Int,
    val tercihler: List<OgrenciHedefTercihDto>,
    val puanTurIds: List<Int>,
    @Json(name = "sıralama") val siralama: Int? = null
)
