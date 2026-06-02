package com.techlife.kockit.domain.auth.model

data class RegisterInfo(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)
