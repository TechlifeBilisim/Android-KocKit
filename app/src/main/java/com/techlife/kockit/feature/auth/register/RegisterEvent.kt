package com.techlife.kockit.feature.auth.register

import com.techlife.kockit.domain.onboarding.model.Gender

sealed interface RegisterEvent {
    data class AccountMethodChanged(val method: RegisterAccountMethod) : RegisterEvent
    data class FullNameChanged(val value: String) : RegisterEvent
    data class EmailChanged(val value: String) : RegisterEvent
    data class NicknameChanged(val value: String) : RegisterEvent
    data class PhoneChanged(val value: String) : RegisterEvent
    data class GenderSelected(val gender: Gender) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent
    data object PasswordVisibilityChanged : RegisterEvent
    data object ConfirmPasswordVisibilityChanged : RegisterEvent
    data object TermsDialogAccepted : RegisterEvent
    data object DataDialogAccepted : RegisterEvent
    data class OtpCodeChanged(val value: String) : RegisterEvent
    data object ResendOtpClicked : RegisterEvent
    data object ContinueClicked : RegisterEvent
    data object LoginClicked : RegisterEvent
    data object BackClicked : RegisterEvent
}
