package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.model.UniversityType

data class GoalSetupUiState(
    val examGoals: List<ExamGoal> = emptyList(),
    val universities: List<University> = emptyList(),
    val availableRegions: List<String> = GoalSetupUniversityCatalog.regions,
    val availableCities: List<String> = emptyList(),
    val departments: List<Department> = emptyList(),
    val aytFieldOptions: List<GoalSetupOption> = GoalSetupAytFields.options,
    val selectedExamGoalId: String? = null,
    val selectedAytFieldId: String? = null,
    val selectedRegion: String? = null,
    val selectedCity: String? = null,
    val selectedUniversityType: UniversityType? = null,
    val selectedUniversityName: String? = null,
    val selectedDepartmentName: String? = null,
    val examError: String? = null,
    val aytFieldError: String? = null,
    val regionError: String? = null,
    val cityError: String? = null,
    val universityTypeError: String? = null,
    val universityError: String? = null,
    val departmentError: String? = null,
    val isLoading: Boolean = false,
    val isDataLoading: Boolean = true,
    val showSuccessDialog: Boolean = false
)

object GoalSetupAytFields {
    val options = listOf(
        GoalSetupOption("sayisal", "Sayısal"),
        GoalSetupOption("sozel", "Sözel"),
        GoalSetupOption("esit_agirlik", "Eşit Ağırlık"),
        GoalSetupOption("dil", "Dil")
    )
}
