package com.techlife.kockit.feature.auth.register

sealed interface RegisterEffect {
    data object NavigateToLogin : RegisterEffect
    data object NavigateToGoalSetup : RegisterEffect
    data object NavigateBack : RegisterEffect
    data object LaunchGoogleSignIn : RegisterEffect
    data class ShowMessage(val message: String) : RegisterEffect
}
