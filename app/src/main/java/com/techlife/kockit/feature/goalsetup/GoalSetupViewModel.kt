package com.techlife.kockit.feature.goalsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.ogrenci.model.CreateOgrenciHedef
import com.techlife.kockit.domain.ogrenci.model.OgrenciHedefTercih
import com.techlife.kockit.domain.ogrenci.usecase.CreateOgrenciHedefUseCase
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.onboarding.usecase.GetExamGoalsUseCase
import com.techlife.kockit.domain.onboarding.usecase.SaveOnboardingInfoUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoBolumlerUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoFakultelerUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoUniversitelerUseCase
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
    private val getYoUniversitelerUseCase: GetYoUniversitelerUseCase,
    private val getYoFakultelerUseCase: GetYoFakultelerUseCase,
    private val getYoBolumlerUseCase: GetYoBolumlerUseCase,
    private val getKullaniciIdUseCase: GetKullaniciIdUseCase,
    private val getOgrenciUseCase: GetOgrenciUseCase,
    private val createOgrenciHedefUseCase: CreateOgrenciHedefUseCase,
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
            val universitelerDeferred = async { getYoUniversitelerUseCase() }
            val kullaniciId = getKullaniciIdUseCase()
            val ogrenciDeferred = async {
                kullaniciId?.takeIf { it.isNotBlank() }?.let { getOgrenciUseCase(it) }
            }

            val universitelerResult = universitelerDeferred.await()
            val ogrenciResult = ogrenciDeferred.await()

            val universiteler = when (universitelerResult) {
                is ApiResult.Success -> universitelerResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(universitelerError = universitelerResult.message) }
                    emptyList()
                }
            }
            val ogrenci = when (ogrenciResult) {
                is ApiResult.Success -> ogrenciResult.data
                is ApiResult.Error, null -> null
            }

            _uiState.update {
                it.copy(
                    examGoals = getExamGoalsUseCase(),
                    universiteler = universiteler,
                    ogrenci = ogrenci,
                    isDataLoading = false
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
            is GoalSetupEvent.UniversityTypeSelected -> _uiState.update {
                it.copy(
                    selectedUniversityType = event.type,
                    universityTypeError = null
                )
            }
            is GoalSetupEvent.UniversitySelected -> {
                _uiState.update {
                    it.copy(
                        selectedUniversityId = event.universityId,
                        selectedUniversityName = event.name,
                        universityError = null,
                        selectedFakulteId = null,
                        selectedFakulteName = null,
                        fakulteler = emptyList(),
                        fakultelerError = null,
                        selectedBolumId = null,
                        selectedBolumName = null,
                        bolumler = emptyList(),
                        isFakultelerLoading = true,
                        isBolumlerLoading = true
                    )
                }
                loadFakulteler(event.universityId)
                loadBolumler(event.universityId)
            }
            is GoalSetupEvent.FakulteSelected -> _uiState.update {
                it.copy(
                    selectedFakulteId = event.fakulteId,
                    selectedFakulteName = event.name,
                    fakulteError = null
                )
            }
            is GoalSetupEvent.BolumSelected -> _uiState.update {
                it.copy(
                    selectedBolumId = event.bolumId,
                    selectedBolumName = event.name,
                    bolumError = null
                )
            }
            GoalSetupEvent.ContinueClicked -> onContinue()
            GoalSetupEvent.BackClicked -> emit(GoalSetupEffect.NavigateBack)
        }
    }

    private fun loadFakulteler(yoUniversiteId: Int) {
        viewModelScope.launch {
            when (val result = getYoFakultelerUseCase(yoUniversiteId)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        fakulteler = result.data,
                        isFakultelerLoading = false,
                        fakultelerError = null
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        fakulteler = emptyList(),
                        isFakultelerLoading = false,
                        fakultelerError = result.message
                    )
                }
            }
        }
    }

    private fun loadBolumler(yoUniversiteId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isBolumlerLoading = true, bolumlerError = null) }
            when (
                val result = getYoBolumlerUseCase(
                    yoBilimId = null,
                    yoUniversiteId = yoUniversiteId
                )
            ) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        bolumler = result.data,
                        isBolumlerLoading = false,
                        bolumlerError = null
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        bolumler = emptyList(),
                        isBolumlerLoading = false,
                        bolumlerError = result.message
                    )
                }
            }
        }
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
        val university = state.universiteler.find { it.id == state.selectedUniversityId }
        val fakulte = state.fakulteler.find { it.id == state.selectedFakulteId }
        val bolum = state.bolumler.find { it.id == state.selectedBolumId }
        val examError = if (exam == null) "Sınav seçimi gerekli" else null
        val aytFieldError = if (state.selectedExamGoalId == "ayt" && state.selectedAytFieldId == null) {
            "AYT alan seçimi gerekli"
        } else {
            null
        }
        val universityTypeError = if (state.selectedUniversityType == null) {
            "Üniversite türü seçimi gerekli"
        } else {
            null
        }
        val universityError = if (university == null) "Üniversite seçimi gerekli" else null
        val fakulteError = if (fakulte == null) "Fakülte seçimi gerekli" else null
        val bolumError = if (bolum == null) "Bölüm seçimi gerekli" else null
        if (
            examError != null ||
            aytFieldError != null ||
            universityTypeError != null ||
            universityError != null ||
            fakulteError != null ||
            bolumError != null
        ) {
            _uiState.update {
                it.copy(
                    examError = examError,
                    aytFieldError = aytFieldError,
                    universityTypeError = universityTypeError,
                    universityError = universityError,
                    fakulteError = fakulteError,
                    bolumError = bolumError
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
        val university = state.universiteler.find { it.id == state.selectedUniversityId }?.let {
            University(
                id = it.id.toString(),
                name = it.name,
                city = "",
                region = "",
                type = state.selectedUniversityType ?: UniversityType.DEVLET
            )
        }
        val department = state.bolumler.find { it.id == state.selectedBolumId }?.let {
            Department(id = it.id.toString(), name = it.name)
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val hedefResult = createRemoteHedefIfPossible(state)
            if (hedefResult is ApiResult.Error) {
                _uiState.update { it.copy(isLoading = false) }
                emit(GoalSetupEffect.ShowMessage(hedefResult.message))
                return@launch
            }

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

    private suspend fun createRemoteHedefIfPossible(state: GoalSetupUiState): ApiResult<Unit>? {
        val universityId = state.selectedUniversityId ?: return null
        val bolumId = state.selectedBolumId ?: return null
        var ogrenciId = state.ogrenci?.ogrenciId
        if (ogrenciId == null) {
            val kullaniciId = getKullaniciIdUseCase() ?: return null
            when (val ogrenciResult = getOgrenciUseCase(kullaniciId)) {
                is ApiResult.Success -> {
                    ogrenciId = ogrenciResult.data.ogrenciId
                    _uiState.update { it.copy(ogrenci = ogrenciResult.data) }
                }
                is ApiResult.Error -> return ogrenciResult
            }
        }
        val resolvedOgrenciId = ogrenciId ?: return null
        return createOgrenciHedefUseCase(
            CreateOgrenciHedef(
                ogrenciId = resolvedOgrenciId,
                tercihler = listOf(
                    OgrenciHedefTercih(
                        yoUniversiteId = universityId,
                        yoBolumId = bolumId
                    )
                ),
                puanTurIds = resolvePuanTurIds(state),
                siralama = null
            )
        )
    }

    private fun resolvePuanTurIds(state: GoalSetupUiState): List<Int> {
        if (state.onlyTyt || state.selectedExamGoalId == "tyt") {
            return listOf(PUAN_TUR_TYT)
        }
        return listOf(
            when (state.selectedAytFieldId) {
                "sayisal" -> PUAN_TUR_SAYISAL
                "sozel" -> PUAN_TUR_SOZEL
                "esit_agirlik" -> PUAN_TUR_EA
                "dil" -> PUAN_TUR_DIL
                else -> PUAN_TUR_SAYISAL
            }
        )
    }

    private fun emit(effect: GoalSetupEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val REQUIRE_SELECTIONS = false
        const val PUAN_TUR_TYT = 1
        const val PUAN_TUR_SAYISAL = 2
        const val PUAN_TUR_SOZEL = 3
        const val PUAN_TUR_EA = 4
        const val PUAN_TUR_DIL = 5
    }
}
