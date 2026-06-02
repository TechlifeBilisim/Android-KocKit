package com.techlife.kockit.feature.auth.login

sealed interface LoginEffect {
    data object NavigateToRegister : LoginEffect
    data object NavigateToHome : LoginEffect
    data class ShowMessage(val message: String) : LoginEffect
}
