package com.techlife.kockit.data.remote.dto.auth

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String? = null
)
