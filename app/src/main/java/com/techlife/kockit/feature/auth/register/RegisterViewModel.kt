package com.techlife.kockit.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.common.Constants
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.RegisterAccountType
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.usecase.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

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
        if (!validateAccountStep()) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (
                val result = registerUseCase(
                    RegisterInfo(
                        accountType = state.accountMethod.toDomainType(),
                        fullName = state.fullName.trim(),
                        email = state.email.trim(),
                        nickname = state.nickname.trim(),
                        phone = state.phone,
                        password = state.password
                    )
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage("Kayıt başarılı."))
                    emit(RegisterEffect.NavigateToGoalSetup)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun validateAccountStep(): Boolean {
        val state = _uiState.value
        val fullNameError = validateRegisterFullName(state.fullName)
        val emailError = validateEmail(state.email)
        val nicknameError = when (state.accountMethod) {
            RegisterAccountMethod.NICKNAME -> {
                if (state.nickname.isBlank()) "Rumuz gerekli" else null
            }
            RegisterAccountMethod.PHONE -> null
        }
        val phoneError = when (state.accountMethod) {
            RegisterAccountMethod.PHONE -> validateTurkishPhone(state.phone)
            RegisterAccountMethod.NICKNAME -> null
        }
        val passwordError = when {
            state.password.length < Constants.MIN_PASSWORD_LENGTH ->
                "Şifre en az ${Constants.MIN_PASSWORD_LENGTH} karakter olmalı"
            else -> null
        }
        val confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Şifre tekrarı gerekli"
            state.confirmPassword != state.password -> "Şifreler eşleşmiyor"
            else -> null
        }
        val termsError = if (!state.isTermsAccepted || !state.isDataAccepted) {
            "Devam etmek için sözleşmeleri kabul etmelisin"
        } else {
            null
        }

        if (
            fullNameError != null ||
            emailError != null ||
            nicknameError != null ||
            phoneError != null ||
            passwordError != null ||
            confirmPasswordError != null ||
            termsError != null
        ) {
            _uiState.update {
                it.copy(
                    fullNameError = fullNameError,
                    emailError = emailError,
                    nicknameError = nicknameError,
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

    private fun RegisterAccountMethod.toDomainType(): RegisterAccountType = when (this) {
        RegisterAccountMethod.NICKNAME -> RegisterAccountType.NICKNAME
        RegisterAccountMethod.PHONE -> RegisterAccountType.PHONE
    }

    private companion object {
        const val RESEND_COOLDOWN_SECONDS = 120
    }
}
