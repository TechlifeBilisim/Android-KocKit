package com.techlife.kockit.feature.goalsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.location.usecase.GetDistrictsUseCase
import com.techlife.kockit.domain.location.usecase.GetProvincesUseCase
import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.usecase.GetExamGoalsUseCase
import com.techlife.kockit.domain.onboarding.usecase.GetUniversitiesUseCase
import com.techlife.kockit.domain.onboarding.usecase.SaveOnboardingInfoUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoBilimlerUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoFakultelerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val getYoBilimlerUseCase: GetYoBilimlerUseCase,
    private val getYoFakultelerUseCase: GetYoFakultelerUseCase,
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

            val provincesDeferred = async { getProvincesUseCase() }
            val bilimlerDeferred = async { getYoBilimlerUseCase() }
            val fakultelerDeferred = async { getYoFakultelerUseCase() }

            val provincesResult = provincesDeferred.await()
            val bilimlerResult = bilimlerDeferred.await()
            val fakultelerResult = fakultelerDeferred.await()

            val provinces = when (provincesResult) {
                is ApiResult.Success -> provincesResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(provincesError = provincesResult.message) }
                    emptyList()
                }
            }
            val bilimler = when (bilimlerResult) {
                is ApiResult.Success -> bilimlerResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(bilimlerError = bilimlerResult.message) }
                    emptyList()
                }
            }
            val fakulteler = when (fakultelerResult) {
                is ApiResult.Success -> fakultelerResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(fakultelerError = fakultelerResult.message) }
                    emptyList()
                }
            }

            _uiState.update { state ->
                applyUniversityFilters(
                    state.copy(
                        examGoals = getExamGoalsUseCase(),
                        provinces = provinces,
                        bilimler = bilimler,
                        fakulteler = fakulteler,
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
            is GoalSetupEvent.OnlyTytToggled -> _uiState.update {
                it.copy(
                    onlyTyt = event.value,
                    selectedExamGoalId = if (event.value) "tyt" else "ayt",
                    examError = null
                )
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
            is GoalSetupEvent.FakulteSelected -> _uiState.update {
                it.copy(
                    selectedFakulteId = event.fakulteId,
                    selectedFakulteName = event.name,
                    fakulteError = null
                )
            }
            is GoalSetupEvent.BilimSelected -> _uiState.update {
                it.copy(
                    selectedBilimId = event.bilimId,
                    selectedBilimName = event.name,
                    bilimError = null
                )
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
        val fakulte = state.fakulteler.find { it.id == state.selectedFakulteId }
        val bilim = state.bilimler.find { it.id == state.selectedBilimId }
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
        val fakulteError = if (fakulte == null) "Fakülte seçimi gerekli" else null
        val bilimError = if (bilim == null) "Bilim seçimi gerekli" else null
        if (
            examError != null ||
            aytFieldError != null ||
            provinceError != null ||
            districtError != null ||
            universityTypeError != null ||
            universityError != null ||
            fakulteError != null ||
            bilimError != null
        ) {
            _uiState.update {
                it.copy(
                    examError = examError,
                    aytFieldError = aytFieldError,
                    provinceError = provinceError,
                    districtError = districtError,
                    universityTypeError = universityTypeError,
                    universityError = universityError,
                    fakulteError = fakulteError,
                    bilimError = bilimError
                )
            }
            return false
        }
        return true
    }

    private fun save() {
        val state = _uiState.value
        val examId = state.selectedExamGoalId ?: if (state.onlyTyt) "tyt" else "ayt"
        val exam = state.examGoals.find { it.id == examId }
        val university = allUniversities.find { it.name == state.selectedUniversityName }
        val bilim = state.bilimler.find { it.id == state.selectedBilimId }
        val department = bilim?.let { Department(id = it.id.toString(), name = it.name) }

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
