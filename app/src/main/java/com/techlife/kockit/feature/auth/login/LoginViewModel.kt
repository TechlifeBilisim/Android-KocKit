package com.techlife.kockit.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.common.Constants
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.usecase.LoginUseCase
import com.techlife.kockit.domain.auth.usecase.LoginWithGoogleUseCase
import com.techlife.kockit.domain.auth.usecase.LoginWithSmsUseCase
import com.techlife.kockit.domain.auth.usecase.RequestLoginSmsUseCase
import com.techlife.kockit.feature.auth.common.validateTurkishPhone
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val requestLoginSmsUseCase: RequestLoginSmsUseCase,
    private val loginWithSmsUseCase: LoginWithSmsUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    private var resendCountdownJob: Job? = null

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginMethodChanged -> {
                resendCountdownJob?.cancel()
                _uiState.update {
                    it.copy(
                        loginMethod = event.method,
                        currentStep = LoginSteps.CREDENTIALS,
                        nicknameError = null,
                        phoneError = null,
                        passwordError = null,
                        otpCode = "",
                        otpError = null,
                        resendSecondsRemaining = 0
                    )
                }
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
            is LoginEvent.OtpCodeChanged -> _uiState.update {
                it.copy(otpCode = event.code.filter(Char::isDigit).take(6), otpError = null)
            }
            LoginEvent.PasswordVisibilityChanged -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            LoginEvent.LoginClicked -> onLoginClicked()
            LoginEvent.ResendOtpClicked -> onResendOtp()
            LoginEvent.BackClicked -> onBack()
            LoginEvent.RegisterClicked -> emit(LoginEffect.NavigateToRegister)
            LoginEvent.ForgotPasswordClicked -> emit(LoginEffect.NavigateToForgotPassword)
            LoginEvent.GoogleLoginClicked -> emit(LoginEffect.LaunchGoogleSignIn)
            LoginEvent.AppleLoginClicked -> emit(LoginEffect.ShowMessage("Apple girişi yakında eklenecek."))
        }
    }

    fun onGoogleSignInSuccess(oAuthIdToken: String?, email: String?) {
        if (oAuthIdToken.isNullOrBlank()) {
            emit(LoginEffect.ShowMessage("Google token alınamadı."))
            return
        }
        if (email.isNullOrBlank()) {
            emit(LoginEffect.ShowMessage("Google e-posta adresi alınamadı."))
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = loginWithGoogleUseCase(oAuthIdToken, email)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage("Giriş başarılı."))
                    emit(LoginEffect.NavigateToGoalSetup)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun onLoginClicked() {
        when (_uiState.value.currentStep) {
            LoginSteps.CREDENTIALS -> submitCredentials()
            LoginSteps.OTP -> submitOtp()
        }
    }

    private fun submitCredentials() {
        when (_uiState.value.loginMethod) {
            LoginMethod.NICKNAME -> submitNicknameLogin()
            LoginMethod.PHONE -> requestPhoneLoginSms()
        }
    }

    private fun submitNicknameLogin() {
        val state = _uiState.value
        val nicknameError = if (state.nickname.isBlank()) "Rumuz gerekli" else null
        val passwordError = when {
            state.password.length < Constants.MIN_PASSWORD_LENGTH ->
                "Şifre en az ${Constants.MIN_PASSWORD_LENGTH} karakter olmalı"
            else -> null
        }
        if (nicknameError != null || passwordError != null) {
            _uiState.update {
                it.copy(nicknameError = nicknameError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = loginUseCase(state.nickname.trim(), state.password)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage("Giriş başarılı."))
                    emit(LoginEffect.NavigateToGoalSetup)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun requestPhoneLoginSms() {
        val state = _uiState.value
        val phoneError = validateTurkishPhone(state.phone)
        if (phoneError != null) {
            _uiState.update { it.copy(phoneError = phoneError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = requestLoginSmsUseCase(state.phone)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentStep = LoginSteps.OTP,
                            otpCode = "",
                            otpError = null
                        )
                    }
                    startResendCountdown()
                    emit(LoginEffect.ShowMessage("Doğrulama kodu telefon numarana gönderildi."))
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun submitOtp() {
        val state = _uiState.value
        val otpError = when {
            state.otpCode.length < 6 -> "6 haneli doğrulama kodunu gir"
            else -> null
        }
        if (otpError != null) {
            _uiState.update { it.copy(otpError = otpError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, otpError = null) }
            when (val result = loginWithSmsUseCase(state.phone, state.otpCode)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(LoginEffect.ShowMessage("Giriş başarılı."))
                    emit(LoginEffect.NavigateToGoalSetup)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, otpError = result.message) }
                    emit(LoginEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun onResendOtp() {
        if (_uiState.value.resendSecondsRemaining > 0) return
        viewModelScope.launch {
            when (val result = requestLoginSmsUseCase(_uiState.value.phone)) {
                is ApiResult.Success -> {
                    startResendCountdown()
                    emit(LoginEffect.ShowMessage("Doğrulama kodu yeniden gönderildi."))
                }
                is ApiResult.Error -> emit(LoginEffect.ShowMessage(result.message))
            }
        }
    }

    private fun onBack() {
        if (_uiState.value.currentStep == LoginSteps.OTP) {
            resendCountdownJob?.cancel()
            _uiState.update {
                it.copy(
                    currentStep = LoginSteps.CREDENTIALS,
                    otpCode = "",
                    otpError = null,
                    resendSecondsRemaining = 0
                )
            }
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

    private fun emit(effect: LoginEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val RESEND_COOLDOWN_SECONDS = 120
    }
}
