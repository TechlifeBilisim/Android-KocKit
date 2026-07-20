package com.techlife.kockit.feature.profilegoals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.ogrenci.model.CalismaTakvimiUpdate
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import com.techlife.kockit.domain.ogrenci.usecase.UpdateCalismaTakvimiUseCase
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaQuery
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanQuery
import com.techlife.kockit.domain.puansiralama.usecase.GetPuanFromSiralamaUseCase
import com.techlife.kockit.domain.puansiralama.usecase.GetSiralamaFromPuanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class ProfileGoalsUiState(
    val isLoading: Boolean = false,
    val isConversionLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPuanTurId: Int = 1,
    val puanInput: String = "",
    val okulPuanInput: String = "",
    val estimatedPuan: Double? = null,
    val estimatedSiralama: Int? = null,
    val conversionMessage: String? = null
)

sealed interface ProfileGoalsEffect {
    data object Completed : ProfileGoalsEffect
    data class ShowMessage(val message: String) : ProfileGoalsEffect
}

@HiltViewModel
class ProfileGoalsViewModel @Inject constructor(
    private val getKullaniciIdUseCase: GetKullaniciIdUseCase,
    private val getOgrenciUseCase: GetOgrenciUseCase,
    private val updateCalismaTakvimiUseCase: UpdateCalismaTakvimiUseCase,
    private val getPuanFromSiralamaUseCase: GetPuanFromSiralamaUseCase,
    private val getSiralamaFromPuanUseCase: GetSiralamaFromPuanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileGoalsUiState())
    val uiState: StateFlow<ProfileGoalsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileGoalsEffect>()
    val effect: SharedFlow<ProfileGoalsEffect> = _effect.asSharedFlow()

    private var conversionJob: Job? = null

    init {
        loadStudentPuanTur()
    }

    fun onPuanTurSelected(puanTurId: Int) {
        _uiState.update {
            it.copy(
                selectedPuanTurId = puanTurId,
                estimatedPuan = null,
                estimatedSiralama = null,
                conversionMessage = null
            )
        }
    }

    fun onPuanInputChanged(value: String) {
        _uiState.update { it.copy(puanInput = value.filterScoreInput(), conversionMessage = null) }
    }

    fun onOkulPuanInputChanged(value: String) {
        _uiState.update { it.copy(okulPuanInput = value.filterScoreInput(), conversionMessage = null) }
    }

    fun onRankGoalSelected(rankGoalId: String) {
        val siralama = rankGoalId.toDoubleOrNull() ?: return
        conversionJob?.cancel()
        conversionJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isConversionLoading = true,
                    estimatedSiralama = siralama.toInt(),
                    estimatedPuan = null,
                    conversionMessage = null,
                    errorMessage = null
                )
            }
            when (
                val result = getPuanFromSiralamaUseCase(
                    SiralamadanPuanQuery(
                        yil = currentYear(),
                        puanTur = _uiState.value.selectedPuanTurId,
                        puanYerlestirmeTur = DEFAULT_PUAN_YERLESTIRME_TUR,
                        siralama = siralama
                    )
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isConversionLoading = false,
                            estimatedPuan = result.data.puan,
                            estimatedSiralama = result.data.siralama?.toInt() ?: siralama.toInt(),
                            conversionMessage = result.data.puan?.let { puan ->
                                "Bu sıralama için tahmini puan: ${formatPuan(puan)}"
                            }
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isConversionLoading = false,
                            conversionMessage = null,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun onCalculateSiralamaFromPuan() {
        val state = _uiState.value
        val puan = state.puanInput.toDoubleOrNull()
        val okulPuan = state.okulPuanInput.toDoubleOrNull()
        if (puan == null) {
            _uiState.update { it.copy(errorMessage = "Puan girin") }
            return
        }
        if (okulPuan == null) {
            _uiState.update { it.copy(errorMessage = "Okul puanı girin") }
            return
        }

        conversionJob?.cancel()
        conversionJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isConversionLoading = true,
                    errorMessage = null,
                    conversionMessage = null
                )
            }
            when (
                val result = getSiralamaFromPuanUseCase(
                    PuandanSiralamaQuery(
                        yil = currentYear(),
                        puanTur = state.selectedPuanTurId,
                        puan = puan,
                        okulPuan = okulPuan
                    )
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isConversionLoading = false,
                            estimatedSiralama = result.data.siralama,
                            estimatedPuan = puan,
                            conversionMessage = "Bu puan için tahmini sıralama: ${
                                formatSiralama(result.data.siralama)
                            }"
                        )
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isConversionLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun submit(studyTimeId: String, rankGoalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val kullaniciId = getKullaniciIdUseCase()
            if (kullaniciId.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false) }
                _effect.emit(ProfileGoalsEffect.ShowMessage("Oturum bulunamadı."))
                return@launch
            }
            when (val ogrenciResult = getOgrenciUseCase(kullaniciId)) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(ProfileGoalsEffect.ShowMessage(ogrenciResult.message))
                    return@launch
                }
                is ApiResult.Success -> Unit
            }

            val dailyHours = studyTimeId.toDailyHours()
            val weeklyHours = dailyHours * 7
            when (
                val result = updateCalismaTakvimiUseCase(
                    CalismaTakvimiUpdate(
                        haftalikCalismaSure = weeklyHours,
                        gunlukOturumSure = dailyHours,
                        genelTekrarGun = 0,
                        gunlukParagrafSeans = 0,
                        gunlukProblemSeans = 0,
                        musaitOlmadigiGun = 0,
                        izinGunler = emptyList()
                    )
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    val siralamaText = _uiState.value.estimatedSiralama
                        ?.let { " Tahmini sıralama: ${formatSiralama(it)}." }
                        .orEmpty()
                    val puanText = _uiState.value.estimatedPuan
                        ?.let { " Tahmini puan: ${formatPuan(it)}." }
                        .orEmpty()
                    _effect.emit(
                        ProfileGoalsEffect.ShowMessage(
                            "Çalışma takvimin güncellendi.$siralamaText$puanText"
                        )
                    )
                    _effect.emit(ProfileGoalsEffect.Completed)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    _effect.emit(ProfileGoalsEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun loadStudentPuanTur() {
        viewModelScope.launch {
            val kullaniciId = getKullaniciIdUseCase() ?: return@launch
            when (val result = getOgrenciUseCase(kullaniciId)) {
                is ApiResult.Success -> {
                    val puanTur = result.data.hazirlikPuanTurId
                    if (puanTur != null && puanTur in 1..5) {
                        _uiState.update { it.copy(selectedPuanTurId = puanTur) }
                    }
                }
                is ApiResult.Error -> Unit
            }
        }
    }

    private fun String.toDailyHours(): Int = when (this) {
        "1h" -> 1
        "2h" -> 2
        "3h" -> 3
        "4h_plus" -> 4
        else -> 1
    }

    private companion object {
        const val PUAN_TUR_TYT = 1
        const val DEFAULT_PUAN_YERLESTIRME_TUR = 1

        fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

        fun String.filterScoreInput(): String =
            filterIndexed { index, char ->
                char.isDigit() || (char == '.' && index > 0 && !contains('.'))
            }

        fun formatPuan(value: Double): String =
            if (value % 1.0 == 0.0) value.toInt().toString() else "%.2f".format(value)

        fun formatSiralama(value: Int): String =
            "%,d".format(value).replace(',', '.')
    }
}
