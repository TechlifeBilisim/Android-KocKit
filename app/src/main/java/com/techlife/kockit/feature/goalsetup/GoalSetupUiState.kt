package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University

data class GoalSetupUiState(
    val examGoals: List<ExamGoal> = emptyList(),
    val universities: List<University> = emptyList(),
    val departments: List<Department> = emptyList(),
    val selectedExamGoalId: String? = null,
    val selectedUniversityName: String? = null,
    val selectedDepartmentName: String? = null,
    val examError: String? = null,
    val universityError: String? = null,
    val departmentError: String? = null,
    val isLoading: Boolean = false,
    val isDataLoading: Boolean = true
)
