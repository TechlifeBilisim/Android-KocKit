package com.techlife.kockit.feature.auth.forgotpassword

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    data class CodeChanged(val value: String) : ForgotPasswordEvent
    data class NewPasswordChanged(val value: String) : ForgotPasswordEvent
    data class ConfirmPasswordChanged(val value: String) : ForgotPasswordEvent
    data object NewPasswordVisibilityChanged : ForgotPasswordEvent
    data object ConfirmPasswordVisibilityChanged : ForgotPasswordEvent
    data object ContinueClicked : ForgotPasswordEvent
    data object BackClicked : ForgotPasswordEvent
    data object ResendCodeClicked : ForgotPasswordEvent
}
