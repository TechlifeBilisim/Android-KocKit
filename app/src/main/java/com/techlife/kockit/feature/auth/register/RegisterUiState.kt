package com.techlife.kockit.feature.auth.register

import com.techlife.kockit.domain.onboarding.model.Gender

data class RegisterUiState(
    val currentStep: Int = RegisterSteps.ACCOUNT,
    val accountMethod: RegisterAccountMethod = RegisterAccountMethod.NICKNAME,
    val fullName: String = "",
    val email: String = "",
    val nickname: String = "",
    val phone: String = "",
    val selectedGender: Gender? = null,
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val isDataAccepted: Boolean = false,
    /** Google hesap seçiminden gelen id token; doluysa kayıt Google endpoint'ine gider. */
    val googleOAuthIdToken: String? = null,
    val isGoogleLinked: Boolean = false,
    val verificationPhone: String = "",
    val otpCode: String = "",
    val otpSentTo: String = "",
    val resendSecondsRemaining: Int = 0,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val nicknameError: String? = null,
    val phoneError: String? = null,
    val genderError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val otpError: String? = null,
    val isLoading: Boolean = false
)
