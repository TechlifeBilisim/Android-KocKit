package com.techlife.kockit.feature.goalsetup

sealed interface GoalSetupEffect {
    data object NavigateToHome : GoalSetupEffect
    data object NavigateBack : GoalSetupEffect
    data class ShowMessage(val message: String) : GoalSetupEffect
}
