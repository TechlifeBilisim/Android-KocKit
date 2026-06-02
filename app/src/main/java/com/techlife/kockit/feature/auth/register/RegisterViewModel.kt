package com.techlife.kockit.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FullNameChanged -> _uiState.update { it.copy(fullName = event.value, fullNameError = null) }
            is RegisterEvent.EmailChanged -> _uiState.update { it.copy(email = event.value, emailError = null) }
            is RegisterEvent.PhoneChanged -> _uiState.update { it.copy(phone = event.value, phoneError = null) }
            is RegisterEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value, passwordError = null) }
            is RegisterEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.value, confirmPasswordError = null) }
            RegisterEvent.PasswordVisibilityChanged -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            RegisterEvent.ConfirmPasswordVisibilityChanged -> _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            is RegisterEvent.TermsCheckedChanged -> _uiState.update { it.copy(isTermsAccepted = event.checked, termsError = null) }
            RegisterEvent.ContinueClicked -> emit(RegisterEffect.NavigateToGoalSetup)
            RegisterEvent.LoginClicked -> emit(RegisterEffect.NavigateToLogin)
            RegisterEvent.BackClicked -> onBack()
        }
    }

    private fun emit(effect: RegisterEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun onGoogleClicked() {
        emit(RegisterEffect.LaunchGoogleSignIn)
    }

    private fun onBack() {
        val step = _uiState.value.currentStep
        if (step > 1) {
            _uiState.update { it.copy(currentStep = step - 1) }
        } else {
            emit(RegisterEffect.NavigateBack)
        }
    }

    // emit moved above
}
