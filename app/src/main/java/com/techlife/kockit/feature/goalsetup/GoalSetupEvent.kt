package com.techlife.kockit.feature.goalsetup

sealed interface GoalSetupEvent {
    data class ExamGoalSelected(val id: String) : GoalSetupEvent
    data class UniversitySelected(val name: String) : GoalSetupEvent
    data class DepartmentSelected(val name: String) : GoalSetupEvent
    data class StudyTimeSelected(val id: String) : GoalSetupEvent
    data class RankGoalSelected(val id: String) : GoalSetupEvent
    data object ContinueClicked : GoalSetupEvent
    data object BackClicked : GoalSetupEvent
}
