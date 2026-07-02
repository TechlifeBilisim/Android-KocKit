package com.techlife.kockit.data.remote.dto.auth

data class RegisterUserRequestDto(
    val ad: String,
    val soyad: String,
    val cepTelefon: String? = null,
    val loginTypeId: Int,
    val rumuz: String? = null,
    val eposta: String,
    val sifre: String
)

data class RegisterUserResponseDto(
    val kullaniciId: String,
    val ad: String,
    val soyad: String,
    val eposta: String,
    val cepTelefon: String?,
    val resim: String?,
    val accessToken: String,
    val refreshToken: String?
)
