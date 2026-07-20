package com.techlife.kockit.domain.ogrenci.model

import com.techlife.kockit.domain.kullanici.model.KullaniciProfile

data class Ogrenci(
    val ogrenciId: Int,
    val kullaniciId: String? = null,
    val kullanici: KullaniciProfile? = null,
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
    val musaitOlmadigiGun: Int? = null,
    val izinGunler: List<Int>? = null
)

data class CalismaTakvimiUpdate(
    val haftalikCalismaSure: Int,
    val gunlukOturumSure: Int,
    val genelTekrarGun: Int = 0,
    val gunlukParagrafSeans: Int = 0,
    val gunlukProblemSeans: Int = 0,
    val musaitOlmadigiGun: Int = 0,
    val izinGunler: List<Int> = emptyList()
)

data class OgrenciHedefTercih(
    val yoUniversiteId: Int,
    val yoBolumId: Int
)

data class CreateOgrenciHedef(
    val ogrenciId: Int,
    val tercihler: List<OgrenciHedefTercih>,
    val puanTurIds: List<Int>,
    val siralama: Int? = null
)
