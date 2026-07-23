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
        emit(RegisterEffect.LaunchGoogleSignIn)
    }

    fun onGoogleAccountSelected(
        oAuthIdToken: String?,
        email: String?,
        displayName: String?,
        givenName: String?,
        familyName: String?,
        phoneNumber: String?
    ) {
        if (oAuthIdToken.isNullOrBlank()) {
            emit(RegisterEffect.ShowMessage("Google token alınamadı."))
            return
        }

        val fullName = buildGoogleFullName(givenName, familyName, displayName)
        val googleEmail = email.orEmpty().trim()
        val googlePhone = normalizeGooglePhone(phoneNumber)

        _uiState.update { state ->
            state.copy(
                googleOAuthIdToken = oAuthIdToken,
                isGoogleLinked = true,
                fullName = fullName.ifBlank { state.fullName },
                fullNameError = null,
                email = googleEmail.ifBlank { state.email },
                emailError = null,
                phone = googlePhone.ifBlank { state.phone },
                phoneError = null
            )
        }
        emit(
            RegisterEffect.ShowMessage(
                if (fullName.isNotBlank() || googleEmail.isNotBlank()) {
                    "Google hesabın bağlandı. Eksik alanları tamamla ve devam et."
                } else {
                    "Google hesabın bağlandı. Bilgilerini tamamla."
                }
            )
        )
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
        val googleToken = state.googleOAuthIdToken?.takeIf { it.isNotBlank() }
        val useGoogleRegister = state.isGoogleLinked || googleToken != null

        if (useGoogleRegister && googleToken == null) {
            emit(RegisterEffect.ShowMessage("Google oturumu eksik. Lütfen Google ile tekrar bağlan."))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val registerInfo = RegisterInfo(
                accountType = RegisterAccountType.PHONE,
                fullName = state.fullName.trim(),
                email = state.email.trim(),
                nickname = state.nickname.trim(),
                phone = state.phone,
                gender = gender
            )
            // Google ile gelindiyse Kayit/Google + oAuthIdToken gönderilir.
            val result = if (googleToken != null) {
                registerWithGoogleUseCase(registerInfo, googleToken)
            } else {
                registerUseCase(registerInfo)
            }
            when (result) {
                is ApiResult.Success -> {
                    val registeredPhone = result.data.phone.orEmpty().ifBlank { state.phone }
                    val successMessage = if (googleToken != null) {
                        "Google ile kayıt başarılı. Telefonuna gönderilen kodu gir."
                    } else {
                        "Kayıt başarılı. Telefonuna gönderilen kodu gir."
                    }
                    continueWithSmsVerification(
                        phone = registeredPhone,
                        successMessage = successMessage
                    )
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emit(RegisterEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun buildGoogleFullName(
        givenName: String?,
        familyName: String?,
        displayName: String?
    ): String {
        val fromParts = listOfNotNull(
            givenName?.trim()?.takeIf { it.isNotBlank() },
            familyName?.trim()?.takeIf { it.isNotBlank() }
        ).joinToString(" ")
        return fromParts.ifBlank { displayName.orEmpty().trim() }
    }

    private fun normalizeGooglePhone(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        val digits = raw.filter(Char::isDigit)
        return when {
            digits.length == 12 && digits.startsWith("90") -> digits.drop(2)
            digits.length == 11 && digits.startsWith("0") -> digits.drop(1)
            digits.length == 10 && digits.startsWith("5") -> digits
            else -> digits.takeLast(10).takeIf { it.length == 10 && it.startsWith("5") }.orEmpty()
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
