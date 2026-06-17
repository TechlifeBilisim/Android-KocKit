package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.UniversityType

sealed interface GoalSetupEvent {
    data class ExamGoalSelected(val id: String) : GoalSetupEvent
    data class AytFieldSelected(val id: String) : GoalSetupEvent
    data class RegionSelected(val name: String) : GoalSetupEvent
    data class CitySelected(val name: String) : GoalSetupEvent
    data class UniversityTypeSelected(val type: UniversityType) : GoalSetupEvent
    data class UniversitySelected(val name: String) : GoalSetupEvent
    data class DepartmentSelected(val name: String) : GoalSetupEvent
    data class StudyTimeSelected(val id: String) : GoalSetupEvent
    data class RankGoalSelected(val id: String) : GoalSetupEvent
    data object ContinueClicked : GoalSetupEvent
    data object BackClicked : GoalSetupEvent
}
