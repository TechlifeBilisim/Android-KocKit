package com.techlife.kockit.domain.auth.model

data class LoginResult(
    val accountVerified: Boolean,
    val registered: Boolean,
    val userId: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val hasStudentGoal: Boolean? = null
)
