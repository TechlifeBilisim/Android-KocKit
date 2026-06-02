package com.techlife.kockit.feature.auth.register

data class RegisterUiState(
    val currentStep: Int = 1,
    val fullName: String = "",
    val email: String = "",
    val nickname: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val nicknameError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val isLoading: Boolean = false
)
