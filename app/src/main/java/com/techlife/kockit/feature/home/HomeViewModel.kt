package com.techlife.kockit.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.auth.usecase.LogoutUseCase
import com.techlife.kockit.domain.auth.usecase.ObserveUserSessionUseCase
import com.techlife.kockit.domain.lesson.usecase.GetLessonsUseCase
import com.techlife.kockit.domain.placement.usecase.ObservePlacementProgressUseCase
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

sealed interface HomeEffect {
    data object NavigateToLogin : HomeEffect
    data class NavigateToPlacement(val sectionKey: String) : HomeEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeUserSessionUseCase: ObserveUserSessionUseCase,
    observePlacementProgressUseCase: ObservePlacementProgressUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getLessonsUseCase: GetLessonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        loadLessons()
        viewModelScope.launch {
            observeUserSessionUseCase().collect { session ->
                _uiState.update {
                    it.copy(
                        fullName = session.fullName.orEmpty(),
                        profileImage = session.profileImage,
                        examGoal = session.selectedExamGoal.orEmpty(),
                        university = session.selectedUniversity.orEmpty(),
                        department = session.selectedDepartment.orEmpty()
                    )
                }
            }
        }
        viewModelScope.launch {
            observePlacementProgressUseCase().collect { progress ->
                _uiState.update {
                    it.copy(
                        showPlacementReminderCard = progress.shouldShowReminderCard,
                        placementSectionKey = progress.nextIncompleteSectionKey,
                        remainingPlacementCount = progress.remainingCount
                    )
                }
            }
        }
    }

    private fun loadLessons() {
        viewModelScope.launch {
            getLessonsUseCase()
        }
    }

    fun onPlacementReminderClick() {
        val sectionKey = _uiState.value.placementSectionKey ?: return
        viewModelScope.launch {
            _effect.emit(HomeEffect.NavigateToPlacement(sectionKey))
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(HomeEffect.NavigateToLogin)
        }
    }
}
