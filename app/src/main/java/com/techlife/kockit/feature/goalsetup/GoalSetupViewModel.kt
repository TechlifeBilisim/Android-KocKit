package com.techlife.kockit.feature.goalsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.usecase.GetDepartmentsUseCase
import com.techlife.kockit.domain.onboarding.usecase.GetExamGoalsUseCase
import com.techlife.kockit.domain.onboarding.usecase.GetUniversitiesUseCase
import com.techlife.kockit.domain.onboarding.usecase.SaveOnboardingInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalSetupViewModel @Inject constructor(
    private val getExamGoalsUseCase: GetExamGoalsUseCase,
    private val getUniversitiesUseCase: GetUniversitiesUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase,
    private val saveOnboardingInfoUseCase: SaveOnboardingInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalSetupUiState())
    val uiState: StateFlow<GoalSetupUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GoalSetupEffect>()
    val effect: SharedFlow<GoalSetupEffect> = _effect.asSharedFlow()

    private var allUniversities: List<University> = emptyList()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            allUniversities = GoalSetupUniversityCatalog.universities
                .ifEmpty { getUniversitiesUseCase() }
            val departments = GoalSetupDepartmentCatalog.departments.ifEmpty { getDepartmentsUseCase() }
            _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        examGoals = getExamGoalsUseCase(),
                        departments = departments,
                        isDataLoading = false
                    )
                )
            }
        }
    }

    fun onEvent(event: GoalSetupEvent) {
        when (event) {
            is GoalSetupEvent.ExamGoalSelected -> _uiState.update {
                it.copy(
                    selectedExamGoalId = event.id,
                    examError = null,
                    selectedAytFieldId = if (event.id == "ayt") it.selectedAytFieldId else null,
                    aytFieldError = null
                )
            }
            is GoalSetupEvent.AytFieldSelected -> _uiState.update {
                it.copy(selectedAytFieldId = event.id, aytFieldError = null)
            }
            is GoalSetupEvent.RegionSelected -> _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        selectedRegion = event.name,
                        selectedCity = null,
                        selectedUniversityName = null,
                        regionError = null,
                        cityError = null,
                        universityError = null
                    )
                )
            }
            is GoalSetupEvent.CitySelected -> _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        selectedCity = event.name,
                        selectedUniversityName = null,
                        cityError = null,
                        universityError = null
                    )
                )
            }
            is GoalSetupEvent.UniversityTypeSelected -> _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        selectedUniversityType = event.type,
                        selectedUniversityName = null,
                        universityTypeError = null,
                        universityError = null
                    )
                )
            }
            is GoalSetupEvent.UniversitySelected -> _uiState.update {
                it.copy(selectedUniversityName = event.name, universityError = null)
            }
            is GoalSetupEvent.DepartmentSelected -> _uiState.update {
                it.copy(selectedDepartmentName = event.name, departmentError = null)
            }
            is GoalSetupEvent.StudyTimeSelected -> _uiState.update {
                it.copy(selectedStudyTimeId = event.id, studyTimeError = null)
            }
            is GoalSetupEvent.RankGoalSelected -> _uiState.update {
                it.copy(selectedRankGoalId = event.id, rankGoalError = null)
            }
            GoalSetupEvent.ContinueClicked -> onContinue()
            GoalSetupEvent.BackClicked -> onBack()
        }
    }

    private fun applyUniversityFilters(state: GoalSetupUiState): GoalSetupUiState {
        val availableCities = allUniversities
            .asSequence()
            .filter { university ->
                state.selectedRegion == null || university.region == state.selectedRegion
            }
            .map { it.city }
            .distinct()
            .sorted()
            .toList()

        val filteredUniversities = allUniversities.filter { university ->
            (state.selectedRegion == null || university.region == state.selectedRegion) &&
                (state.selectedCity == null || university.city == state.selectedCity) &&
                (state.selectedUniversityType == null || university.type == state.selectedUniversityType)
        }

        val selectedCity = state.selectedCity?.takeIf { it in availableCities }
        val selectedUniversityName = state.selectedUniversityName?.takeIf { name ->
            filteredUniversities.any { it.name == name }
        }

        return state.copy(
            availableCities = availableCities,
            universities = filteredUniversities,
            selectedCity = selectedCity,
            selectedUniversityName = selectedUniversityName
        )
    }

    private fun onBack() {
        val step = _uiState.value.currentStep
        if (step > GoalSetupSteps.EXAM_AND_TARGET) {
            _uiState.update { it.copy(currentStep = step - 1) }
        } else {
            emit(GoalSetupEffect.NavigateBack)
        }
    }

    private fun onContinue() {
        when (_uiState.value.currentStep) {
            GoalSetupSteps.EXAM_AND_TARGET -> if (validateStep1()) {
                _uiState.update { it.copy(currentStep = GoalSetupSteps.STUDY_TIME) }
            }
            GoalSetupSteps.STUDY_TIME -> if (validateStep2()) {
                _uiState.update { it.copy(currentStep = GoalSetupSteps.RANK_GOAL) }
            }
            GoalSetupSteps.RANK_GOAL -> if (validateStep3()) {
                save()
            }
        }
    }

    private fun validateStep1(): Boolean {
        val state = _uiState.value
        val exam = state.examGoals.find { it.id == state.selectedExamGoalId }
        val university = state.universities.find { it.name == state.selectedUniversityName }
        val department = state.departments.find { it.name == state.selectedDepartmentName }
        val examError = if (exam == null) "Sınav seçimi gerekli" else null
        val aytFieldError = if (state.selectedExamGoalId == "ayt" && state.selectedAytFieldId == null) {
            "AYT alan seçimi gerekli"
        } else {
            null
        }
        val regionError = if (state.selectedRegion == null) "Bölge seçimi gerekli" else null
        val cityError = if (state.selectedCity == null) "İl seçimi gerekli" else null
        val universityTypeError = if (state.selectedUniversityType == null) {
            "Üniversite türü seçimi gerekli"
        } else {
            null
        }
        val universityError = if (university == null) "Üniversite seçimi gerekli" else null
        val departmentError = if (department == null) "Bölüm seçimi gerekli" else null
        if (
            examError != null ||
            aytFieldError != null ||
            regionError != null ||
            cityError != null ||
            universityTypeError != null ||
            universityError != null ||
            departmentError != null
        ) {
            _uiState.update {
                it.copy(
                    examError = examError,
                    aytFieldError = aytFieldError,
                    regionError = regionError,
                    cityError = cityError,
                    universityTypeError = universityTypeError,
                    universityError = universityError,
                    departmentError = departmentError
                )
            }
            return false
        }
        return true
    }

    private fun validateStep2(): Boolean {
        val studyTimeError = if (_uiState.value.selectedStudyTimeId == null) {
            "Çalışma süresi seçimi gerekli"
        } else {
            null
        }
        if (studyTimeError != null) {
            _uiState.update { it.copy(studyTimeError = studyTimeError) }
            return false
        }
        return true
    }

    private fun validateStep3(): Boolean {
        val rankGoalError = if (_uiState.value.selectedRankGoalId == null) {
            "Hedef seçimi gerekli"
        } else {
            null
        }
        if (rankGoalError != null) {
            _uiState.update { it.copy(rankGoalError = rankGoalError) }
            return false
        }
        return true
    }

    private fun save() {
        val state = _uiState.value
        val exam = state.examGoals.find { it.id == state.selectedExamGoalId } ?: return
        val university = allUniversities.find { it.name == state.selectedUniversityName } ?: return
        val department = state.departments.find { it.name == state.selectedDepartmentName } ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            saveOnboardingInfoUseCase(OnboardingInfo(exam, university, department))
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(GoalSetupEffect.NavigateToHome)
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(GoalSetupEffect.ShowMessage("Kayıt başarısız."))
                }
        }
    }

    private fun emit(effect: GoalSetupEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
