package com.techlife.kockit.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.auth.usecase.LogoutUseCase
import com.techlife.kockit.domain.auth.usecase.ObserveUserSessionUseCase
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
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeUserSessionUseCase: ObserveUserSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            observeUserSessionUseCase().collect { session ->
                _uiState.update {
                    it.copy(
                        fullName = session.fullName.orEmpty(),
                        examGoal = session.selectedExamGoal.orEmpty(),
                        university = session.selectedUniversity.orEmpty(),
                        department = session.selectedDepartment.orEmpty()
                    )
                }
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(HomeEffect.NavigateToLogin)
        }
    }
}
