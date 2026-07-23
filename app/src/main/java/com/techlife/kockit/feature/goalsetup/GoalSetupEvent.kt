package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.UniversityType

sealed interface GoalSetupEvent {
    data class OnlyTytToggled(val value: Boolean) : GoalSetupEvent
    data class PuanTurSelected(val puanTurId: Int) : GoalSetupEvent
    data class SiralamaChanged(val value: String) : GoalSetupEvent
    data class UniversityTypeSelected(val type: UniversityType?) : GoalSetupEvent
    data class UniversitySelected(val universityId: Int, val name: String) : GoalSetupEvent
    data class BolumSelected(val bolumId: Int, val name: String) : GoalSetupEvent
    data object ContinueClicked : GoalSetupEvent
    data object BackClicked : GoalSetupEvent
    data object SuccessDialogDismissed : GoalSetupEvent
    data object GoToPlacementClicked : GoalSetupEvent
    data object GoToMainClicked : GoalSetupEvent
}
