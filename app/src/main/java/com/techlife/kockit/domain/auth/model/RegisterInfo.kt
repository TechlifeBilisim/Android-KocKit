package com.techlife.kockit.domain.auth.model

enum class RegisterAccountType {
    NICKNAME,
    PHONE
}

data class RegisterInfo(
    val accountType: RegisterAccountType,
    val fullName: String,
    val email: String,
    val nickname: String,
    val phone: String,
    val password: String
)

data class RegisterResult(
    val userId: String,
    val accessToken: String,
    val refreshToken: String?,
    val email: String? = null,
    val phone: String? = null
)
