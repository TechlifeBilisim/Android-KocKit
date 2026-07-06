package com.techlife.kockit.feature.goalsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.location.usecase.GetDistrictsUseCase
import com.techlife.kockit.domain.location.usecase.GetProvincesUseCase
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
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val saveOnboardingInfoUseCase: SaveOnboardingInfoUseCase
) : ViewModel() {

    private companion object {
        // Geçici: hedef seçimleri zorunlu değil.
        const val REQUIRE_SELECTIONS = false
    }

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
            val provinces = when (val result = getProvincesUseCase()) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(provincesError = result.message) }
                    emptyList()
                }
            }
            _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        examGoals = getExamGoalsUseCase(),
                        departments = departments,
                        provinces = provinces,
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
            GoalSetupEvent.SuccessDialogDismissed -> _uiState.update {
                it.copy(showSuccessDialog = false)
            }
            GoalSetupEvent.GoToPlacementClicked -> {
                _uiState.update { it.copy(showSuccessDialog = false) }
                emit(GoalSetupEffect.NavigateToPlacement)
            }
            GoalSetupEvent.GoToMainClicked -> {
                _uiState.update { it.copy(showSuccessDialog = false) }
                emit(GoalSetupEffect.NavigateToMain)
            }
            is GoalSetupEvent.ProvinceSelected -> {
                _uiState.update { state ->
                    applyUniversityFilters(
                        state.copy(
                            selectedProvinceId = event.provinceId,
                            selectedProvinceName = event.name,
                            selectedDistrictId = null,
                            selectedDistrictName = null,
                            districts = emptyList(),
                            districtsError = null,
                            provinceError = null,
                            districtError = null,
                            selectedUniversityName = null,
                            universityError = null,
                            isDistrictsLoading = true
                        )
                    )
                }
                loadDistricts(event.provinceId)
            }
            is GoalSetupEvent.DistrictSelected -> _uiState.update {
                it.copy(
                    selectedDistrictId = event.districtId,
                    selectedDistrictName = event.name,
                    districtError = null
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
            GoalSetupEvent.ContinueClicked -> onContinue()
            GoalSetupEvent.BackClicked -> emit(GoalSetupEffect.NavigateBack)
        }
    }

    private fun loadDistricts(provinceId: Int) {
        viewModelScope.launch {
            when (val result = getDistrictsUseCase(provinceId)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        districts = result.data,
                        isDistrictsLoading = false,
                        districtsError = null
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        districts = emptyList(),
                        isDistrictsLoading = false,
                        districtsError = result.message
                    )
                }
            }
        }
    }

    private fun applyUniversityFilters(state: GoalSetupUiState): GoalSetupUiState {
        val filteredUniversities = allUniversities.filter { university ->
            (state.selectedProvinceName == null ||
                university.city.equals(state.selectedProvinceName, ignoreCase = true)) &&
                (state.selectedUniversityType == null || university.type == state.selectedUniversityType)
        }

        val selectedProvinceName = state.selectedProvinceName?.takeIf { name ->
            state.provinces.any { it.name.equals(name, ignoreCase = true) }
        }
        val selectedUniversityName = state.selectedUniversityName?.takeIf { name ->
            filteredUniversities.any { it.name == name }
        }

        return state.copy(
            universities = filteredUniversities,
            selectedProvinceName = selectedProvinceName,
            selectedUniversityName = selectedUniversityName
        )
    }

    private fun onContinue() {
        if (!REQUIRE_SELECTIONS || validate()) {
            save()
        }
    }

    private fun validate(): Boolean {
        if (!REQUIRE_SELECTIONS) return true

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
        val provinceError = if (state.selectedProvinceName == null) "İl seçimi gerekli" else null
        val districtError = if (state.selectedDistrictName == null) "İlçe seçimi gerekli" else null
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
            provinceError != null ||
            districtError != null ||
            universityTypeError != null ||
            universityError != null ||
            departmentError != null
        ) {
            _uiState.update {
                it.copy(
                    examError = examError,
                    aytFieldError = aytFieldError,
                    provinceError = provinceError,
                    districtError = districtError,
                    universityTypeError = universityTypeError,
                    universityError = universityError,
                    departmentError = departmentError
                )
            }
            return false
        }
        return true
    }

    private fun save() {
        val state = _uiState.value
        val exam = state.examGoals.find { it.id == state.selectedExamGoalId }
        val university = allUniversities.find { it.name == state.selectedUniversityName }
        val department = state.departments.find { it.name == state.selectedDepartmentName }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (exam != null && university != null && department != null) {
                saveOnboardingInfoUseCase(OnboardingInfo(exam, university, department))
                    .onSuccess {
                        _uiState.update { it.copy(isLoading = false, showSuccessDialog = true) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                        emit(GoalSetupEffect.ShowMessage("Kayıt başarısız."))
                    }
            } else {
                _uiState.update { it.copy(isLoading = false, showSuccessDialog = true) }
            }
        }
    }

    private fun emit(effect: GoalSetupEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
