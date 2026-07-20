package com.techlife.kockit.domain.kullanici.model

data class UpdateKullaniciProfile(
    val ad: String,
    val soyad: String,
    val rumuz: String,
    val eposta: String,
    val cinsiyetId: Int,
    val ilId: Int,
    val ilceId: Int,
    val resim: String? = null,
    val uygulamaTemaId: Int? = null
)

data class KullaniciProfile(
    val kullaniciId: String,
    val ad: String? = null,
    val soyad: String? = null,
    val rumuz: String? = null,
    val eposta: String? = null,
    val cepTelefon: String? = null,
    val cinsiyetId: Int? = null,
    val ilId: Int? = null,
    val ilceId: Int? = null,
    val resim: String? = null,
    val uygulamaTemaId: Int? = null
)
