package com.techlife.kockit.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.RegisterAccountType
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.usecase.RegisterUseCase
import com.techlife.kockit.domain.auth.usecase.RegisterWithGoogleUseCase
import com.techlife.kockit.domain.auth.usecase.SendSmsCodeUseCase
import com.techlife.kockit.domain.auth.usecase.VerifySmsCodeUseCase
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
    private val registerUseCase: RegisterUseCase,
    private val registerWithGoogleUseCase: RegisterWithGoogleUseCase,
    private val sendSmsCodeUseCase: SendSmsCodeUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase
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
            is RegisterEvent.PhoneChanged -> _uiState.update {
                it.copy(phone = event.value.filter(Char::isDigit).take(11), phoneError = null)
            }
            is RegisterEvent.GenderSelected -> _uiState.update {
                it.copy(selectedGender = event.gender, genderError = null)
            }
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
        if (!validateAccountStep()) return
        emit(RegisterEffect.LaunchGoogleSignIn)
    }

    fun onGoogleSignInSuccess(oAuthIdToken: String?, email: String?) {
        if (oAuthIdToken.isNullOrBlank()) {
            emit(RegisterEffect.ShowMessage("Google token alınamadı."))
            return
        }
        val state = _uiState.value
        val gender = state.selectedGender
        if (gender == null) {
            _uiState.update { it.copy(genderError = "Cinsiyet seçimi gerekli") }
            return
        }
        val registerEmail = email?.takeIf { it.isNotBlank() } ?: state.email.trim()
        if (registerEmail.isBlank()) {
            emit(RegisterEffect.ShowMessage("Google e-posta adresi alınamadı."))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (
                val result = registerWithGoogleUseCase(
                    RegisterInfo(
                        accountType = RegisterAccountType.PHONE,
                        fullName = state.fullName.trim(),
                        email = registerEmail,
                        nickname = state.nickname.trim(),
                        phone = state.phone,
                        gender = gender
                    ),
                    oAuthIdToken = oAuthIdToken
                )
            ) {
                is ApiResult.Success -> {
                    val registeredPhone = result.data.phone.orEmpty().ifBlank { state.phone }
                    continueWithSmsVerification(
                        phone = registeredPhone,
                        successMessage = "Google ile kayıt başarılı. Telefonuna gönderilen kodu gir."
                    )
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun onContinue() {
        when (_uiState.value.currentStep) {
            RegisterSteps.ACCOUNT -> submitAccountStep()
            RegisterSteps.OTP -> submitOtp()
        }
    }

    private fun submitAccountStep() {
        if (!validateAccountStep()) return

        val state = _uiState.value
        val gender = state.selectedGender ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (
                val result = registerUseCase(
                    RegisterInfo(
                        accountType = RegisterAccountType.PHONE,
                        fullName = state.fullName.trim(),
                        email = state.email.trim(),
                        nickname = state.nickname.trim(),
                        phone = state.phone,
                        gender = gender
                    )
                )
            ) {
                is ApiResult.Success -> {
                    val registeredPhone = result.data.phone.orEmpty().ifBlank { state.phone }
                    continueWithSmsVerification(
                        phone = registeredPhone,
                        successMessage = "Kayıt başarılı. Telefonuna gönderilen kodu gir."
                    )
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private suspend fun continueWithSmsVerification(phone: String, successMessage: String) {
        _uiState.update { it.copy(verificationPhone = phone) }
        when (val smsResult = sendSmsCodeUseCase(phone)) {
            is ApiResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentStep = RegisterSteps.OTP,
                        otpCode = "",
                        otpError = null,
                        otpSentTo = maskPhoneDestination(phone)
                    )
                }
                startResendCountdown()
                emit(RegisterEffect.ShowMessage(successMessage))
            }
            is ApiResult.Error -> {
                _uiState.update { it.copy(isLoading = false) }
                emit(RegisterEffect.ShowMessage(smsResult.message))
            }
        }
    }

    private fun validateAccountStep(): Boolean {
        val state = _uiState.value
        val fullNameError = validateRegisterFullName(state.fullName)
        val emailError = validateEmail(state.email)
        val nicknameError = if (state.nickname.isBlank()) "Rumuz gerekli" else null
        val phoneError = validateTurkishPhone(state.phone)
        val genderError = if (state.selectedGender == null) "Cinsiyet seçimi gerekli" else null
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
            genderError != null ||
            termsError != null
        ) {
            _uiState.update {
                it.copy(
                    fullNameError = fullNameError,
                    emailError = emailError,
                    nicknameError = nicknameError,
                    phoneError = phoneError,
                    genderError = genderError,
                    termsError = termsError
                )
            }
            return false
        }
        return true
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
            when (val result = verifySmsCodeUseCase(state.verificationPhone, state.otpCode)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage("Hesabın doğrulandı."))
                    emit(RegisterEffect.NavigateToGoalSetup)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, otpError = result.message) }
                    emit(RegisterEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun onResendOtp() {
        if (_uiState.value.resendSecondsRemaining > 0) return
        val phone = _uiState.value.verificationPhone
        viewModelScope.launch {
            when (val result = sendSmsCodeUseCase(phone)) {
                is ApiResult.Success -> {
                    startResendCountdown()
                    emit(RegisterEffect.ShowMessage("Doğrulama kodu telefon numarana yeniden gönderildi."))
                }
                is ApiResult.Error -> emit(RegisterEffect.ShowMessage(result.message))
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

    private fun onBack() {
        when (_uiState.value.currentStep) {
            RegisterSteps.ACCOUNT -> emit(RegisterEffect.NavigateBack)
            RegisterSteps.OTP -> {
                resendCountdownJob?.cancel()
                _uiState.update {
                    it.copy(
                        currentStep = RegisterSteps.ACCOUNT,
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
