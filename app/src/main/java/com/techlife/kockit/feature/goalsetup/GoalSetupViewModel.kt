package com.techlife.kockit.feature.goalsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
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

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    examGoals = getExamGoalsUseCase(),
                    universities = getUniversitiesUseCase(),
                    departments = getDepartmentsUseCase(),
                    isDataLoading = false
                )
            }
        }
    }

    fun onEvent(event: GoalSetupEvent) {
        when (event) {
            is GoalSetupEvent.ExamGoalSelected -> _uiState.update { it.copy(selectedExamGoalId = event.id, examError = null) }
            is GoalSetupEvent.UniversitySelected -> _uiState.update { it.copy(selectedUniversityName = event.name, universityError = null) }
            is GoalSetupEvent.DepartmentSelected -> _uiState.update { it.copy(selectedDepartmentName = event.name, departmentError = null) }
            GoalSetupEvent.ContinueClicked -> save()
            GoalSetupEvent.BackClicked -> emit(GoalSetupEffect.NavigateBack)
        }
    }

    private fun save() {
        val state = _uiState.value
        val exam = state.examGoals.find { it.id == state.selectedExamGoalId }
        val university = state.universities.find { it.name == state.selectedUniversityName }
        val department = state.departments.find { it.name == state.selectedDepartmentName }
        val examError = if (exam == null) "Sınav seçimi gerekli" else null
        val universityError = if (university == null) "Üniversite seçimi gerekli" else null
        val departmentError = if (department == null) "Bölüm seçimi gerekli" else null
        if (examError != null || universityError != null || departmentError != null) {
            _uiState.update {
                it.copy(examError = examError, universityError = universityError, departmentError = departmentError)
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            saveOnboardingInfoUseCase(OnboardingInfo(exam!!, university!!, department!!))
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
