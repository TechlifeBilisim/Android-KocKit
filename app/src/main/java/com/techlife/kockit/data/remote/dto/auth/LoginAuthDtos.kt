package com.techlife.kockit.data.remote.dto.auth

import com.squareup.moshi.Json

data class StudentRegisterRequestDto(
    val ad: String,
    val soyad: String,
    val cepTelefon: String? = null,
    val loginTypeId: Int,
    val eposta: String,
    val sifre: String? = null,
    val cinsiyetId: Int = 1,
    val ilId: Int = 1,
    val ilceId: Int = 1,
    val rumuz: String? = null
)

data class StudentRegisterResponseDto(
    val kullaniciId: String,
    val ad: String,
    val soyad: String,
    val eposta: String,
    val cepTelefon: String? = null,
    val cinsiyetId: Int? = null,
    val resim: String? = null,
    val accessToken: String,
    val refreshToken: String? = null
)

data class NicknameLoginRequestDto(
    val rumuz: String,
    val sifre: String
)

data class LoginSmsRequestDto(
    @Json(name = "ceptelefon") val cepTelefon: String
)

data class LoginSmsVerifyRequestDto(
    val cepTelefon: String,
    val kod: String
)

data class GoogleLoginRequestDto(
    val oAuthIdToken: String,
    val email: String
)

data class TechpassLoginRequestDto(
    val xTechOturum: String
)

data class RefreshTokenRequestDto(
    val refreshToken: String
)

data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String? = null
)

data class LoginInitResponseDto(
    @Json(name = "hesapOnaylandı") val hesapOnaylandi: Boolean? = null,
    @Json(name = "kayıtlı") val kayitli: Boolean? = null,
    val kullaniciProfili: AuthSessionDto? = null
)

data class GoogleLoginResponseDto(
    @Json(name = "hesapOnaylandı") val hesapOnaylandi: Boolean? = null,
    @Json(name = "kayıtlı") val kayitli: Boolean? = null,
    val kullaniciProfili: AuthSessionDto? = null,
    val kullaniciId: String? = null,
    val ad: String? = null,
    val soyad: String? = null,
    val resim: String? = null,
    val eposta: String? = null,
    val cepTelefon: String? = null,
    val cinsiyetId: Int? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

data class AuthSessionDto(
    val kullaniciId: String? = null,
    val ad: String? = null,
    val soyad: String? = null,
    val resim: String? = null,
    val eposta: String? = null,
    val cepTelefon: String? = null,
    val cinsiyetId: Int? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

data class SendSmsCodeRequestDto(
    val cepTelefon: String
)

data class VerifySmsCodeRequestDto(
    val cepTelefon: String,
    val kod: String
)

data class SendEmailCodeRequestDto(
    val eposta: String
)

data class VerifyEmailCodeRequestDto(
    val eposta: String,
    val kod: String
)
