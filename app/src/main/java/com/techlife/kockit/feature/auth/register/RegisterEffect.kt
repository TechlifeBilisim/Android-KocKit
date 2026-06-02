package com.techlife.kockit.feature.auth.register

sealed interface RegisterEffect {
    data object NavigateToLogin : RegisterEffect
    data object NavigateToHome : RegisterEffect
    data object NavigateBack : RegisterEffect
    data class ShowMessage(val message: String) : RegisterEffect
}
