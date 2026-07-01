package com.techlife.kockit.feature.auth.register

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
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    private var resendCountdownJob: Job? = null

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.AccountMethodChanged -> _uiState.update {
                it.copy(
                    accountMethod = event.method,
                    nicknameError = null,
                    phoneError = null
                )
            }
            is RegisterEvent.FullNameChanged -> _uiState.update { it.copy(fullName = event.value, fullNameError = null) }
            is RegisterEvent.EmailChanged -> _uiState.update { it.copy(email = event.value, emailError = null) }
            is RegisterEvent.NicknameChanged -> _uiState.update { it.copy(nickname = event.value, nicknameError = null) }
            is RegisterEvent.PhoneChanged -> _uiState.update { it.copy(phone = event.value, phoneError = null) }
            is RegisterEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value, passwordError = null) }
            is RegisterEvent.ConfirmPasswordChanged -> _uiState.update {
                it.copy(confirmPassword = event.value, confirmPasswordError = null)
            }
            RegisterEvent.PasswordVisibilityChanged -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            RegisterEvent.ConfirmPasswordVisibilityChanged -> _uiState.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            RegisterEvent.TermsDialogAccepted -> _uiState.update {
                it.copy(isTermsAccepted = true, termsError = null)
            }
            RegisterEvent.DataDialogAccepted -> _uiState.update {
                it.copy(isDataAccepted = true)
            }
            is RegisterEvent.VerificationChannelChanged -> _uiState.update {
                it.copy(
                    verificationChannel = event.channel,
                    verificationEmailError = null,
                    verificationPhoneError = null
                )
            }
            is RegisterEvent.VerificationEmailChanged -> _uiState.update {
                it.copy(verificationEmail = event.value, verificationEmailError = null)
            }
            is RegisterEvent.VerificationPhoneChanged -> _uiState.update {
                it.copy(
                    verificationPhone = event.value.filter(Char::isDigit).take(11),
                    verificationPhoneError = null
                )
            }
            is RegisterEvent.OtpCodeChanged -> _uiState.update {
                it.copy(otpCode = event.value.filter(Char::isDigit).take(6), otpError = null)
            }
            RegisterEvent.ResendOtpClicked -> onResendOtp()
            RegisterEvent.ContinueClicked -> onContinue()
            RegisterEvent.LoginClicked -> emit(RegisterEffect.NavigateToLogin)
            RegisterEvent.BackClicked -> onBack()
        }
    }

    fun onGoogleClicked() {
        emit(RegisterEffect.LaunchGoogleSignIn)
    }

    private fun onContinue() {
        when (_uiState.value.currentStep) {
            RegisterSteps.ACCOUNT -> submitAccountStep()
            RegisterSteps.VERIFICATION -> submitVerificationContact()
            RegisterSteps.OTP -> submitOtp()
        }
    }

    private fun submitAccountStep() {
        val state = _uiState.value
        val verificationChannel = when (state.accountMethod) {
            RegisterAccountMethod.PHONE -> RegisterVerificationChannel.PHONE
            RegisterAccountMethod.NICKNAME -> state.verificationChannel
        }
        _uiState.update {
            it.copy(
                currentStep = RegisterSteps.VERIFICATION,
                verificationChannel = verificationChannel,
                verificationEmail = it.email.trim(),
                verificationPhone = when (state.accountMethod) {
                    RegisterAccountMethod.PHONE -> state.phone
                    RegisterAccountMethod.NICKNAME -> if (it.verificationPhone.isBlank()) it.phone else it.verificationPhone
                },
                fullNameError = null,
                emailError = null,
                nicknameError = null,
                phoneError = null,
                passwordError = null,
                confirmPasswordError = null,
                termsError = null
            )
        }
    }

    private fun submitVerificationContact() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(400)
            val destination = maskVerificationDestination(
                channel = state.verificationChannel,
                email = state.verificationEmail,
                phone = state.verificationPhone
            )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentStep = RegisterSteps.OTP,
                    otpCode = "",
                    otpError = null,
                    otpSentTo = destination,
                    verificationEmailError = null,
                    verificationPhoneError = null
                )
            }
            startResendCountdown()
            val message = when (state.verificationChannel) {
                RegisterVerificationChannel.EMAIL -> "Doğrulama kodu e-posta adresine gönderildi."
                RegisterVerificationChannel.PHONE -> "Doğrulama kodu telefon numarana gönderildi."
            }
            emit(RegisterEffect.ShowMessage(message))
        }
    }

    private fun submitOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, otpError = null) }
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
            emit(RegisterEffect.ShowMessage("Hesabın doğrulandı."))
            emit(RegisterEffect.NavigateToGoalSetup)
        }
    }

    private fun onResendOtp() {
        if (_uiState.value.resendSecondsRemaining > 0) return
        viewModelScope.launch {
            startResendCountdown()
            val message = when (_uiState.value.verificationChannel) {
                RegisterVerificationChannel.EMAIL -> "Doğrulama kodu e-posta adresine yeniden gönderildi."
                RegisterVerificationChannel.PHONE -> "Doğrulama kodu telefon numarana yeniden gönderildi."
            }
            emit(RegisterEffect.ShowMessage(message))
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

    private fun onBack() {
        when (_uiState.value.currentStep) {
            RegisterSteps.ACCOUNT -> emit(RegisterEffect.NavigateBack)
            RegisterSteps.VERIFICATION -> _uiState.update { it.copy(currentStep = RegisterSteps.ACCOUNT) }
            RegisterSteps.OTP -> {
                resendCountdownJob?.cancel()
                _uiState.update {
                    it.copy(
                        currentStep = RegisterSteps.VERIFICATION,
                        otpCode = "",
                        otpError = null,
                        resendSecondsRemaining = 0
                    )
                }
            }
        }
    }

    override fun onCleared() {
        resendCountdownJob?.cancel()
        super.onCleared()
    }

    private fun emit(effect: RegisterEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val RESEND_COOLDOWN_SECONDS = 120
    }
}
