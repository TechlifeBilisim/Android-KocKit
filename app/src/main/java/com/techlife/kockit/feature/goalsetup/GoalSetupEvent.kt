package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.Gender
import com.techlife.kockit.domain.onboarding.model.UniversityType

sealed interface GoalSetupEvent {
    data class ExamGoalSelected(val id: String) : GoalSetupEvent
    data class AytFieldSelected(val id: String) : GoalSetupEvent
    data class OnlyTytToggled(val value: Boolean) : GoalSetupEvent
    data class GenderSelected(val gender: Gender) : GoalSetupEvent
    data class ProvinceSelected(val provinceId: Int, val name: String) : GoalSetupEvent
    data class DistrictSelected(val districtId: Int, val name: String) : GoalSetupEvent
    data class UniversityTypeSelected(val type: UniversityType?) : GoalSetupEvent
    data class UniversitySelected(val name: String) : GoalSetupEvent
    data class DepartmentSelected(val name: String) : GoalSetupEvent
    data object ContinueClicked : GoalSetupEvent
    data object BackClicked : GoalSetupEvent
    data object SuccessDialogDismissed : GoalSetupEvent
    data object GoToPlacementClicked : GoalSetupEvent
    data object GoToMainClicked : GoalSetupEvent
}
