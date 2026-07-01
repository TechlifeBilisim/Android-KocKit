package com.techlife.kockit.feature.auth.login

data class LoginUiState(
    val loginMethod: LoginMethod = LoginMethod.NICKNAME,
    val nickname: String = "",
    val phone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val nicknameError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false
)
