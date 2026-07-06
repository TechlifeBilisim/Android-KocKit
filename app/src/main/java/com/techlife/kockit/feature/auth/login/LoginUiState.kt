package com.techlife.kockit.feature.auth.login

data class LoginUiState(
    val currentStep: Int = LoginSteps.CREDENTIALS,
    val loginMethod: LoginMethod = LoginMethod.NICKNAME,
    val nickname: String = "",
    val phone: String = "",
    val password: String = "",
    val otpCode: String = "",
    val resendSecondsRemaining: Int = 0,
    val isPasswordVisible: Boolean = false,
    val nicknameError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val otpError: String? = null,
    val isLoading: Boolean = false
)
