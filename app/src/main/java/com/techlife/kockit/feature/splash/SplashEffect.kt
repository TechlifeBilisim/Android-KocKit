package com.techlife.kockit.feature.splash

sealed interface SplashEffect {
    data object NavigateToLogin : SplashEffect
    data object NavigateToRegister : SplashEffect
}
