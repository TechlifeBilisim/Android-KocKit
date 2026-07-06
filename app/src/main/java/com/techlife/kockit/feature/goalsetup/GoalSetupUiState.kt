package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province
import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.model.UniversityType

data class GoalSetupUiState(
    val examGoals: List<ExamGoal> = emptyList(),
    val universities: List<University> = emptyList(),
    val provinces: List<Province> = emptyList(),
    val districts: List<District> = emptyList(),
    val departments: List<Department> = emptyList(),
    val aytFieldOptions: List<GoalSetupOption> = GoalSetupAytFields.options,
    val selectedExamGoalId: String? = null,
    val selectedAytFieldId: String? = null,
    val selectedProvinceId: Int? = null,
    val selectedProvinceName: String? = null,
    val selectedDistrictId: Int? = null,
    val selectedDistrictName: String? = null,
    val selectedUniversityType: UniversityType? = null,
    val selectedUniversityName: String? = null,
    val selectedDepartmentName: String? = null,
    val examError: String? = null,
    val aytFieldError: String? = null,
    val provinceError: String? = null,
    val districtError: String? = null,
    val universityTypeError: String? = null,
    val universityError: String? = null,
    val departmentError: String? = null,
    val provincesError: String? = null,
    val districtsError: String? = null,
    val isDistrictsLoading: Boolean = false,
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
