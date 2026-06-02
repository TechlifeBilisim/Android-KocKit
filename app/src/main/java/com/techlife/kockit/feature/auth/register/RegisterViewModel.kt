package com.techlife.kockit.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.util.ValidationHelper
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.usecase.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

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
            RegisterEvent.ContinueClicked -> onContinue()
            RegisterEvent.LoginClicked -> emit(RegisterEffect.NavigateToLogin)
            RegisterEvent.BackClicked -> onBack()
        }
    }

    private fun onBack() {
        val step = _uiState.value.currentStep
        if (step > 1) {
            _uiState.update { it.copy(currentStep = step - 1) }
        } else {
            emit(RegisterEffect.NavigateBack)
        }
    }

    private fun onContinue() {
        when (_uiState.value.currentStep) {
            1 -> if (validateStep1()) _uiState.update { it.copy(currentStep = 2) }
            2 -> _uiState.update { it.copy(currentStep = 3) }
            3 -> register()
        }
    }

    private fun validateStep1(): Boolean {
        val state = _uiState.value
        val fullNameError = if (!ValidationHelper.isNotBlank(state.fullName)) "Ad soyad gerekli" else null
        val emailError = when {
            !ValidationHelper.isNotBlank(state.email) -> "E-posta gerekli"
            !ValidationHelper.isValidEmail(state.email) -> "Geçerli bir e-posta girin"
            else -> null
        }
        val phoneError = if (!ValidationHelper.isNotBlank(state.phone)) "Telefon gerekli" else null
        val passwordError = when {
            !ValidationHelper.isValidPassword(state.password) -> "Şifre en az 6 karakter olmalı"
            else -> null
        }
        val confirmPasswordError = when {
            !ValidationHelper.doPasswordsMatch(state.password, state.confirmPassword) -> "Şifreler eşleşmiyor"
            else -> null
        }
        val termsError = if (!state.isTermsAccepted) "Kullanım koşullarını kabul etmelisiniz" else null
        if (listOf(fullNameError, emailError, phoneError, passwordError, confirmPasswordError, termsError).any { it != null }) {
            _uiState.update {
                it.copy(
                    fullNameError = fullNameError,
                    emailError = emailError,
                    phoneError = phoneError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError,
                    termsError = termsError
                )
            }
            return false
        }
        return true
    }

    private fun register() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            registerUseCase(
                RegisterInfo(
                    fullName = state.fullName.trim(),
                    email = state.email.trim(),
                    phoneNumber = state.phone.trim(),
                    password = state.password
                )
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                emit(RegisterEffect.NavigateToHome)
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                emit(RegisterEffect.ShowMessage("Kayıt başarısız."))
            }
        }
    }

    private fun emit(effect: RegisterEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
