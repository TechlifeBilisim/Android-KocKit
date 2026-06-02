package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University

data class GoalSetupUiState(
    val currentStep: Int = 1,
    val examGoals: List<ExamGoal> = emptyList(),
    val universities: List<University> = emptyList(),
    val departments: List<Department> = emptyList(),
    val studyTimeOptions: List<GoalSetupOption> = GoalSetupSteps.studyTimeOptions,
    val rankGoalOptions: List<GoalSetupOption> = GoalSetupSteps.rankGoalOptions,
    val selectedExamGoalId: String? = null,
    val selectedUniversityName: String? = null,
    val selectedDepartmentName: String? = null,
    val selectedStudyTimeId: String? = null,
    val selectedRankGoalId: String? = null,
    val examError: String? = null,
    val universityError: String? = null,
    val departmentError: String? = null,
    val studyTimeError: String? = null,
    val rankGoalError: String? = null,
    val isLoading: Boolean = false,
    val isDataLoading: Boolean = true
)

object GoalSetupSteps {
    const val EXAM_AND_TARGET = 1
    const val STUDY_TIME = 2
    const val RANK_GOAL = 3

    val studyTimeOptions = listOf(
        GoalSetupOption("1h", "1 Saat"),
        GoalSetupOption("2h", "2 Saat"),
        GoalSetupOption("3h", "3 Saat"),
        GoalSetupOption("4h_plus", "4+ Saat")
    )

    val rankGoalOptions = listOf(
        GoalSetupOption("50000", "ilk 50.000"),
        GoalSetupOption("20000", "ilk 20.000"),
        GoalSetupOption("10000", "ilk 10.000"),
        GoalSetupOption("1000", "ilk 1.000")
    )
}
