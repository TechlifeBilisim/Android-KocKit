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
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.onboarding.usecase.SaveOnboardingInfoUseCase
import com.techlife.kockit.domain.yo.usecase.GetYoBolumlerUseCase
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
    private val getYoUniversitelerUseCase: GetYoUniversitelerUseCase,
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
            val universitelerDeferred = async {
                getYoUniversitelerUseCase(_uiState.value.selectedUniversityType?.id)
            }
            val bolumlerDeferred = async { getYoBolumlerUseCase(yoBilimId = null, yoUniversiteId = null) }
            val kullaniciId = getKullaniciIdUseCase()
            val ogrenciDeferred = async {
                kullaniciId?.takeIf { it.isNotBlank() }?.let { getOgrenciUseCase(it) }
            }

            val universitelerResult = universitelerDeferred.await()
            val bolumlerResult = bolumlerDeferred.await()
            val ogrenciResult = ogrenciDeferred.await()

            val universiteler = when (universitelerResult) {
                is ApiResult.Success -> universitelerResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(universitelerError = universitelerResult.message) }
                    emptyList()
                }
            }
            val bolumler = when (bolumlerResult) {
                is ApiResult.Success -> bolumlerResult.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(bolumlerError = bolumlerResult.message) }
                    emptyList()
                }
            }
            val ogrenci = when (ogrenciResult) {
                is ApiResult.Success -> ogrenciResult.data
                is ApiResult.Error, null -> null
            }

            _uiState.update {
                it.copy(
                    universiteler = universiteler,
                    bolumler = bolumler,
                    ogrenci = ogrenci,
                    isBolumlerLoading = false,
                    isUniversitelerLoading = false,
                    isDataLoading = false
                )
            }
        }
    }

    private fun loadUniversiteler(unversiteTurId: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUniversitelerLoading = true,
                    universitelerError = null,
                    selectedUniversityId = null,
                    selectedUniversityName = null
                )
            }
            when (val result = getYoUniversitelerUseCase(unversiteTurId)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        universiteler = result.data,
                        isUniversitelerLoading = false
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        universiteler = emptyList(),
                        universitelerError = result.message,
                        isUniversitelerLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: GoalSetupEvent) {
        when (event) {
            is GoalSetupEvent.OnlyTytToggled -> _uiState.update {
                it.copy(
                    onlyTyt = event.value,
                    selectedPuanTurId = if (event.value) GoalSetupPuanTurOptions.PUAN_TUR_TYT else it.selectedPuanTurId,
                    puanTurError = null
                )
            }
            is GoalSetupEvent.PuanTurSelected -> {
                if (_uiState.value.onlyTyt) return
                _uiState.update {
                    it.copy(
                        selectedPuanTurId = event.puanTurId,
                        onlyTyt = event.puanTurId == GoalSetupPuanTurOptions.PUAN_TUR_TYT,
                        puanTurError = null
                    )
                }
            }
            is GoalSetupEvent.SiralamaChanged -> _uiState.update {
                it.copy(
                    siralamaInput = event.value.filter { char -> char.isDigit() },
                    siralamaError = null
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
            is GoalSetupEvent.UniversityTypeSelected -> {
                if (_uiState.value.selectedUniversityType == event.type) return
                _uiState.update {
                    it.copy(
                        selectedUniversityType = event.type,
                        universityTypeError = null
                    )
                }
                loadUniversiteler(event.type?.id)
            }
            is GoalSetupEvent.UniversitySelected -> _uiState.update {
                it.copy(
                    selectedUniversityId = event.universityId,
                    selectedUniversityName = event.name,
                    universityError = null
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

    private fun onContinue() {
        if (validate()) {
            save()
        }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        val puanTurError = if (state.selectedPuanTurId == null) "Puan türü seçimi gerekli" else null
        val siralamaError = if (parseSiralama(state.siralamaInput) == null) {
            "Hedef sıralama gerekli"
        } else {
            null
        }
        val universityError = if (state.selectedUniversityId == null) "Üniversite seçimi gerekli" else null
        val bolumError = if (state.selectedBolumId == null) "Bölüm seçimi gerekli" else null

        if (puanTurError != null || siralamaError != null || universityError != null || bolumError != null) {
            _uiState.update {
                it.copy(
                    puanTurError = puanTurError,
                    siralamaError = siralamaError,
                    universityError = universityError,
                    bolumError = bolumError
                )
            }
            return false
        }
        return true
    }

    private fun save() {
        val state = _uiState.value
        val puanTurId = state.selectedPuanTurId ?: return
        val siralama = parseSiralama(state.siralamaInput) ?: return
        val university = state.universiteler.find { it.id == state.selectedUniversityId }
        val bolum = state.bolumler.find { it.id == state.selectedBolumId }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val hedefResult = createRemoteHedefIfPossible(state, siralama)
            if (hedefResult is ApiResult.Error) {
                _uiState.update { it.copy(isLoading = false) }
                emit(GoalSetupEffect.ShowMessage(hedefResult.message))
                return@launch
            }

            if (university != null && bolum != null) {
                val examGoal = examGoalForPuanTur(puanTurId)
                saveOnboardingInfoUseCase(
                    OnboardingInfo(
                        examGoal = examGoal,
                        university = University(
                            id = university.id.toString(),
                            name = university.name,
                            city = "",
                            region = "",
                            type = state.selectedUniversityType ?: UniversityType.DEVLET
                        ),
                        department = Department(
                            id = bolum.id.toString(),
                            name = bolum.name
                        )
                    )
                ).onSuccess {
                    _uiState.update { it.copy(isLoading = false, showSuccessDialog = true) }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(GoalSetupEffect.ShowMessage("Kayıt başarısız."))
                }
            } else {
                _uiState.update { it.copy(isLoading = false, showSuccessDialog = true) }
            }
        }
    }

    private suspend fun createRemoteHedefIfPossible(
        state: GoalSetupUiState,
        siralama: Int
    ): ApiResult<Unit>? {
        val universityId = state.selectedUniversityId ?: return null
        val bolumId = state.selectedBolumId ?: return null
        val puanTurId = state.selectedPuanTurId ?: return null

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
                puanTurIds = listOf(puanTurId),
                siralama = siralama
            )
        )
    }

    private fun examGoalForPuanTur(puanTurId: Int): ExamGoal = when (puanTurId) {
        GoalSetupPuanTurOptions.PUAN_TUR_TYT -> ExamGoal(
            id = "tyt",
            title = "TYT",
            subtitle = "Temel Yeterlilik Testi",
            type = "TYT"
        )
        else -> ExamGoal(
            id = "ayt",
            title = "AYT",
            subtitle = "Alan Yeterlilik Testleri",
            type = "AYT"
        )
    }

    private fun parseSiralama(raw: String): Int? =
        raw.filter(Char::isDigit).toIntOrNull()?.takeIf { it > 0 }

    private fun emit(effect: GoalSetupEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
