package com.techlife.kockit.feature.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
class ForgotPasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ForgotPasswordEffect>()
    val effect: SharedFlow<ForgotPasswordEffect> = _effect.asSharedFlow()

    private var resendCountdownJob: Job? = null

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> _uiState.update {
                it.copy(email = event.value, emailError = null)
            }
            is ForgotPasswordEvent.CodeChanged -> _uiState.update {
                it.copy(code = event.value.filter(Char::isDigit).take(6), codeError = null)
            }
            is ForgotPasswordEvent.NewPasswordChanged -> _uiState.update {
                it.copy(newPassword = event.value, newPasswordError = null)
            }
            is ForgotPasswordEvent.ConfirmPasswordChanged -> _uiState.update {
                it.copy(confirmPassword = event.value, confirmPasswordError = null)
            }
            ForgotPasswordEvent.NewPasswordVisibilityChanged -> _uiState.update {
                it.copy(isNewPasswordVisible = !it.isNewPasswordVisible)
            }
            ForgotPasswordEvent.ConfirmPasswordVisibilityChanged -> _uiState.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            ForgotPasswordEvent.ContinueClicked -> onContinue()
            ForgotPasswordEvent.BackClicked -> onBack()
            ForgotPasswordEvent.ResendCodeClicked -> onResendCode()
        }
    }

    private fun onContinue() {
        when (_uiState.value.currentStep) {
            ForgotPasswordSteps.EMAIL -> submitEmail()
            ForgotPasswordSteps.CODE -> submitCode()
            ForgotPasswordSteps.NEW_PASSWORD -> submitNewPassword()
        }
    }

    private fun submitEmail() {
        val email = _uiState.value.email.trim()
        val emailError = when {
            email.isBlank() -> "E-posta adresi gerekli"
            !email.contains("@") || !email.contains(".") -> "Geçerli bir e-posta adresi gir"
            else -> null
        }
        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(400)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentStep = ForgotPasswordSteps.CODE,
                    code = ""
                )
            }
            startResendCountdown()
            emit(ForgotPasswordEffect.ShowMessage("Doğrulama kodu e-posta adresine gönderildi."))
        }
    }

    private fun submitCode() {
        val code = _uiState.value.code
        val codeError = when {
            code.length < 6 -> "6 haneli doğrulama kodunu gir"
            else -> null
        }
        if (codeError != null) {
            _uiState.update { it.copy(codeError = codeError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(400)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentStep = ForgotPasswordSteps.NEW_PASSWORD,
                    newPassword = "",
                    confirmPassword = ""
                )
            }
        }
    }

    private fun submitNewPassword() {
        val state = _uiState.value
        val newPasswordError = when {
            state.newPassword.length < 6 -> "Şifre en az 6 karakter olmalı"
            else -> null
        }
        val confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Şifre tekrarı gerekli"
            state.confirmPassword != state.newPassword -> "Şifreler eşleşmiyor"
            else -> null
        }
        if (newPasswordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    newPasswordError = newPasswordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
            emit(ForgotPasswordEffect.ShowMessage("Şifren başarıyla güncellendi."))
            emit(ForgotPasswordEffect.NavigateToLogin)
        }
    }

    private fun onResendCode() {
        if (_uiState.value.resendSecondsRemaining > 0) return
        viewModelScope.launch {
            emit(ForgotPasswordEffect.ShowMessage("Doğrulama kodu yeniden gönderildi."))
            startResendCountdown()
        }
    }

    private fun startResendCountdown() {
        resendCountdownJob?.cancel()
        _uiState.update { it.copy(resendSecondsRemaining = RESEND_COOLDOWN_SECONDS) }
        resendCountdownJob = viewModelScope.launch {
            while (_uiState.value.resendSecondsRemaining > 0) {
                delay(1_000)
                _uiState.update { state ->
                    state.copy(resendSecondsRemaining = (state.resendSecondsRemaining - 1).coerceAtLeast(0))
                }
            }
        }
    }

    override fun onCleared() {
        resendCountdownJob?.cancel()
        super.onCleared()
    }

    private fun onBack() {
        when (_uiState.value.currentStep) {
            ForgotPasswordSteps.EMAIL -> emit(ForgotPasswordEffect.NavigateBack)
            ForgotPasswordSteps.CODE -> _uiState.update {
                it.copy(currentStep = ForgotPasswordSteps.EMAIL, code = "", codeError = null)
            }
            ForgotPasswordSteps.NEW_PASSWORD -> _uiState.update {
                it.copy(
                    currentStep = ForgotPasswordSteps.CODE,
                    newPassword = "",
                    confirmPassword = "",
                    newPasswordError = null,
                    confirmPasswordError = null
                )
            }
        }
    }

    private fun emit(effect: ForgotPasswordEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val RESEND_COOLDOWN_SECONDS = 178
    }
}
