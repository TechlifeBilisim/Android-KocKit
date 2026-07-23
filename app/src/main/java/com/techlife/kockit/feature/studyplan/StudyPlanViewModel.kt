package com.techlife.kockit.feature.studyplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.ogrenci.model.CalismaTakvimiUpdate
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import com.techlife.kockit.domain.ogrenci.usecase.UpdateCalismaTakvimiUseCase
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
class StudyPlanViewModel @Inject constructor(
    private val getKullaniciIdUseCase: GetKullaniciIdUseCase,
    private val getOgrenciUseCase: GetOgrenciUseCase,
    private val updateCalismaTakvimiUseCase: UpdateCalismaTakvimiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyPlanUiState())
    val uiState: StateFlow<StudyPlanUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<StudyPlanEffect>()
    val effect: SharedFlow<StudyPlanEffect> = _effect.asSharedFlow()

    init {
        loadPlan()
    }

    fun onEvent(event: StudyPlanEvent) {
        when (event) {
            is StudyPlanEvent.DayHoursChanged -> _uiState.update { state ->
                state.copy(
                    days = state.days.mapIndexed { index, day ->
                        if (index == event.index) day.copy(hours = event.hours.coerceIn(0, 12)) else day
                    }
                )
            }
            is StudyPlanEvent.SessionMinutesChanged -> _uiState.update {
                it.copy(sessionMinutes = event.value.coerceIn(15, 180))
            }
            is StudyPlanEvent.ParagraphMinutesChanged -> _uiState.update {
                it.copy(paragraphMinutes = event.value.coerceIn(0, 180))
            }
            is StudyPlanEvent.ProblemMinutesChanged -> _uiState.update {
                it.copy(problemMinutes = event.value.coerceIn(0, 180))
            }
            is StudyPlanEvent.RevisionDayChanged -> _uiState.update {
                it.copy(revisionDay = event.value)
            }
            is StudyPlanEvent.UnavailableDayToggled -> _uiState.update { state ->
                val next = if (event.shortName in state.unavailableDays) {
                    state.unavailableDays - event.shortName
                } else {
                    state.unavailableDays + event.shortName
                }
                state.copy(unavailableDays = next)
            }
            is StudyPlanEvent.SpecialDateAdded -> _uiState.update {
                it.copy(specialDates = it.specialDates + event.date)
            }
            is StudyPlanEvent.SpecialDateRemoved -> _uiState.update {
                it.copy(specialDates = it.specialDates.filterNot { date -> date.id == event.id })
            }
            StudyPlanEvent.DaysEditClicked -> _uiState.update { it.copy(daysEditing = true) }
            StudyPlanEvent.DaysSaveClicked -> _uiState.update { it.copy(daysEditing = false) }
            StudyPlanEvent.ParametersEditClicked -> _uiState.update { it.copy(parametersEditing = true) }
            StudyPlanEvent.ParametersSaveClicked -> _uiState.update { it.copy(parametersEditing = false) }
            StudyPlanEvent.UnavailableEditClicked -> _uiState.update { it.copy(unavailableEditing = true) }
            StudyPlanEvent.UnavailableSaveClicked -> _uiState.update { it.copy(unavailableEditing = false) }
            StudyPlanEvent.SaveClicked -> savePlan()
        }
    }

    private fun loadPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val kullaniciId = getKullaniciIdUseCase()
            if (kullaniciId.isNullOrBlank()) {
                _uiState.update { it.copy(isLoading = false, isDataLoaded = true) }
                _effect.emit(StudyPlanEffect.ShowMessage("Oturum bulunamadı."))
                return@launch
            }
            when (val result = getOgrenciUseCase(kullaniciId)) {
                is ApiResult.Success -> {
                    _uiState.update { applyOgrenci(it, result.data).copy(isLoading = false, isDataLoaded = true) }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, isDataLoaded = true) }
                    _effect.emit(StudyPlanEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun savePlan() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update {
                it.copy(
                    isSaving = true,
                    daysEditing = false,
                    parametersEditing = false,
                    unavailableEditing = false
                )
            }

            val revisionDayIndex = StudyPlanDayMapper.dayIndexFromFullName(state.revisionDay) ?: 7
            val izinGunler = StudyPlanDayMapper.shortNamesToDayIndexes(state.unavailableDays)
            val update = CalismaTakvimiUpdate(
                haftalikCalismaSure = state.totalHours,
                gunlukOturumSure = state.sessionMinutes,
                genelTekrarGun = revisionDayIndex,
                gunlukParagrafSeans = state.paragraphMinutes,
                gunlukProblemSeans = state.problemMinutes,
                musaitOlmadigiGun = izinGunler.firstOrNull() ?: 0,
                izinGunler = izinGunler
            )

            when (val result = updateCalismaTakvimiUseCase(update)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        applyOgrenci(it, result.data).copy(isSaving = false)
                    }
                    _effect.emit(StudyPlanEffect.ShowMessage("Çalışma takvimin güncellendi."))
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.emit(StudyPlanEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun applyOgrenci(state: StudyPlanUiState, ogrenci: Ogrenci): StudyPlanUiState {
        val unavailable = StudyPlanDayMapper.dayIndexesToShortNames(ogrenci.izinGunler.orEmpty())
        val weeklyHours = ogrenci.haftalikCalismaSure
        val days = if (weeklyHours != null && weeklyHours > 0) {
            StudyPlanDayMapper.distributeWeeklyHours(weeklyHours, unavailable)
        } else {
            state.days.ifEmpty { StudyPlanDayMapper.defaultDays() }
        }
        return state.copy(
            days = days,
            sessionMinutes = ogrenci.gunlukOturumSure
                ?.takeIf { it > 0 }
                ?: state.sessionMinutes,
            paragraphMinutes = ogrenci.gunlukParagrafSeans
                ?.takeIf { it >= 0 }
                ?: state.paragraphMinutes,
            problemMinutes = ogrenci.gunlukProblemSeans
                ?.takeIf { it >= 0 }
                ?: state.problemMinutes,
            revisionDay = ogrenci.genelTekrarGun
                ?.let(StudyPlanDayMapper::fullNameFromDayIndex)
                ?: state.revisionDay,
            unavailableDays = unavailable
        )
    }
}
