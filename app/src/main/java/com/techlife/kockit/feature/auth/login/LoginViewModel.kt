package com.techlife.kockit.feature.auth.login

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
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginMethodChanged -> _uiState.update {
                it.copy(
                    loginMethod = event.method,
                    nicknameError = null,
                    phoneError = null,
                    passwordError = null
                )
            }
            is LoginEvent.NicknameChanged -> _uiState.update {
                it.copy(nickname = event.nickname, nicknameError = null)
            }
            is LoginEvent.PhoneChanged -> _uiState.update {
                it.copy(
                    phone = event.phone.filter(Char::isDigit).take(11),
                    phoneError = null
                )
            }
            is LoginEvent.PasswordChanged -> _uiState.update {
                it.copy(password = event.password, passwordError = null)
            }
            LoginEvent.PasswordVisibilityChanged -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            LoginEvent.LoginClicked -> emit(LoginEffect.NavigateToGoalSetup)
            LoginEvent.RegisterClicked -> emit(LoginEffect.NavigateToRegister)
            LoginEvent.ForgotPasswordClicked -> emit(LoginEffect.NavigateToForgotPassword)
            LoginEvent.GoogleLoginClicked -> emit(LoginEffect.LaunchGoogleSignIn)
            LoginEvent.AppleLoginClicked -> emit(LoginEffect.ShowMessage("Apple girişi yakında eklenecek."))
        }
    }

    private fun emit(effect: LoginEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
