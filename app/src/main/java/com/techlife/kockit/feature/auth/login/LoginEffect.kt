package com.techlife.kockit.feature.auth.login

sealed interface LoginEffect {
    data object NavigateToRegister : LoginEffect
    data object NavigateToGoalSetup : LoginEffect
    data object NavigateToMain : LoginEffect
    data object LaunchGoogleSignIn : LoginEffect
    data class ShowMessage(val message: String) : LoginEffect
}
