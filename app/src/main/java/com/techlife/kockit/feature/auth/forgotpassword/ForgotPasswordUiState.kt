package com.techlife.kockit.feature.auth.forgotpassword

data class ForgotPasswordUiState(
    val currentStep: Int = ForgotPasswordSteps.EMAIL,
    val email: String = "",
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val codeError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val resendSecondsRemaining: Int = 0
)

object ForgotPasswordSteps {
    const val EMAIL = 1
    const val CODE = 2
    const val NEW_PASSWORD = 3
}
