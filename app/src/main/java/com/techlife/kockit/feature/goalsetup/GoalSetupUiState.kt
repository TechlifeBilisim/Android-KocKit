package com.techlife.kockit.feature.goalsetup

import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoUniversite

data class GoalSetupUiState(
    val universiteler: List<YoUniversite> = emptyList(),
    val bolumler: List<YoBolum> = emptyList(),
    val ogrenci: Ogrenci? = null,
    val onlyTyt: Boolean = false,
    val selectedPuanTurId: Int? = null,
    val siralamaInput: String = "",
    val selectedUniversityType: UniversityType? = null,
    val selectedUniversityId: Int? = null,
    val selectedUniversityName: String? = null,
    val selectedBolumId: Int? = null,
    val selectedBolumName: String? = null,
    val puanTurError: String? = null,
    val siralamaError: String? = null,
    val universityTypeError: String? = null,
    val universityError: String? = null,
    val bolumError: String? = null,
    val universitelerError: String? = null,
    val bolumlerError: String? = null,
    val isBolumlerLoading: Boolean = false,
    val isUniversitelerLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isDataLoading: Boolean = true,
    val showSuccessDialog: Boolean = false
)
