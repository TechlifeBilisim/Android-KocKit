package com.techlife.kockit.feature.auth.forgotpassword

sealed interface ForgotPasswordEffect {
    data object NavigateBack : ForgotPasswordEffect
    data object NavigateToLogin : ForgotPasswordEffect
    data object Completed : ForgotPasswordEffect
    data class ShowMessage(val message: String) : ForgotPasswordEffect
}
