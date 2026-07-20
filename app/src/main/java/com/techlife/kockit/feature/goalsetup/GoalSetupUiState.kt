package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite

data class GoalSetupUiState(
    val examGoals: List<ExamGoal> = emptyList(),
    val universiteler: List<YoUniversite> = emptyList(),
    val fakulteler: List<YoFakulte> = emptyList(),
    val bolumler: List<YoBolum> = emptyList(),
    val ogrenci: Ogrenci? = null,
    val aytFieldOptions: List<GoalSetupOption> = GoalSetupAytFields.options,
    val onlyTyt: Boolean = false,
    val selectedExamGoalId: String? = null,
    val selectedAytFieldId: String? = null,
    val selectedUniversityType: UniversityType? = null,
    val selectedUniversityId: Int? = null,
    val selectedUniversityName: String? = null,
    val selectedFakulteId: Int? = null,
    val selectedFakulteName: String? = null,
    val selectedBolumId: Int? = null,
    val selectedBolumName: String? = null,
    val examError: String? = null,
    val aytFieldError: String? = null,
    val universityTypeError: String? = null,
    val universityError: String? = null,
    val fakulteError: String? = null,
    val bolumError: String? = null,
    val universitelerError: String? = null,
    val fakultelerError: String? = null,
    val bolumlerError: String? = null,
    val isFakultelerLoading: Boolean = false,
    val isBolumlerLoading: Boolean = false,
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
