package com.techlife.kockit.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.util.ValidationHelper
import com.techlife.kockit.domain.auth.usecase.LoginUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> _uiState.update { it.copy(email = event.email, emailError = null) }
            is LoginEvent.PasswordChanged -> _uiState.update { it.copy(password = event.password, passwordError = null) }
            LoginEvent.PasswordVisibilityChanged -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginEvent.LoginClicked -> login()
            LoginEvent.RegisterClicked -> emit(LoginEffect.NavigateToRegister)
            LoginEvent.ForgotPasswordClicked -> emit(LoginEffect.ShowMessage("Şifre sıfırlama yakında eklenecek."))
            LoginEvent.GoogleLoginClicked -> emit(LoginEffect.ShowMessage("Google girişi yakında eklenecek."))
            LoginEvent.AppleLoginClicked -> emit(LoginEffect.ShowMessage("Apple girişi yakında eklenecek."))
        }
    }

    private fun login() {
        val state = _uiState.value
        val emailError = when {
            !ValidationHelper.isNotBlank(state.email) -> "E-posta gerekli"
            !ValidationHelper.isValidEmail(state.email) -> "Geçerli bir e-posta girin"
            else -> null
        }
        val passwordError = when {
            !ValidationHelper.isNotBlank(state.password) -> "Şifre gerekli"
            !ValidationHelper.isValidPassword(state.password) -> "Şifre en az 6 karakter olmalı"
            else -> null
        }
        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loginUseCase(state.email.trim(), state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.NavigateToHome)
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage("Giriş başarısız. Bilgilerinizi kontrol edin."))
                }
        }
    }

    private fun emit(effect: LoginEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
