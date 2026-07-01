package com.techlife.kockit.feature.auth.login

sealed interface LoginEvent {
    data class LoginMethodChanged(val method: LoginMethod) : LoginEvent
    data class NicknameChanged(val nickname: String) : LoginEvent
    data class PhoneChanged(val phone: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object PasswordVisibilityChanged : LoginEvent
    data object LoginClicked : LoginEvent
    data object ForgotPasswordClicked : LoginEvent
    data object RegisterClicked : LoginEvent
    data object GoogleLoginClicked : LoginEvent
    data object AppleLoginClicked : LoginEvent
}
