package com.techlife.kockit.feature.auth.forgotpassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitLogo
import com.techlife.kockit.core.designsystem.component.KocKitOtpCodeField
import com.techlife.kockit.core.designsystem.component.KocKitPasswordField
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = KocKitTheme.extraColors

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ForgotPasswordEffect.NavigateBack -> onNavigateBack()
                ForgotPasswordEffect.NavigateToLogin -> onNavigateToLogin()
                is ForgotPasswordEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    KocKitBackground(useFormBackgroundImage = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            KocKitLogo()

            Spacer(modifier = Modifier.height(32.dp))

            when (uiState.currentStep) {
                ForgotPasswordSteps.EMAIL -> ForgotPasswordEmailStep(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    isLoading = uiState.isLoading,
                    onEmailChange = { viewModel.onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                    onContinue = { viewModel.onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onBack = { viewModel.onEvent(ForgotPasswordEvent.BackClicked) }
                )
                ForgotPasswordSteps.CODE -> ForgotPasswordCodeStep(
                    email = uiState.email,
                    code = uiState.code,
                    codeError = uiState.codeError,
                    isLoading = uiState.isLoading,
                    onCodeChange = { viewModel.onEvent(ForgotPasswordEvent.CodeChanged(it)) },
                    onContinue = { viewModel.onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onBack = { viewModel.onEvent(ForgotPasswordEvent.BackClicked) }
                )
                ForgotPasswordSteps.NEW_PASSWORD -> ForgotPasswordNewPasswordStep(
                    newPassword = uiState.newPassword,
                    confirmPassword = uiState.confirmPassword,
                    newPasswordError = uiState.newPasswordError,
                    confirmPasswordError = uiState.confirmPasswordError,
                    isNewPasswordVisible = uiState.isNewPasswordVisible,
                    isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                    isLoading = uiState.isLoading,
                    onNewPasswordChange = { viewModel.onEvent(ForgotPasswordEvent.NewPasswordChanged(it)) },
                    onConfirmPasswordChange = { viewModel.onEvent(ForgotPasswordEvent.ConfirmPasswordChanged(it)) },
                    onNewPasswordVisibilityToggle = {
                        viewModel.onEvent(ForgotPasswordEvent.NewPasswordVisibilityChanged)
                    },
                    onConfirmPasswordVisibilityToggle = {
                        viewModel.onEvent(ForgotPasswordEvent.ConfirmPasswordVisibilityChanged)
                    },
                    onContinue = { viewModel.onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onBack = { viewModel.onEvent(ForgotPasswordEvent.BackClicked) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ForgotPasswordEmailStep(
    email: String,
    emailError: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitBoldText(
        text = "Şifremi Unuttum",
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    KocKitText(
        text = "E-posta adresini gir, sana doğrulama kodu gönderelim.",
        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
        lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(32.dp))

    KocKitTextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = "E-posta adresin",
        leadingIconVector = Icons.Filled.Email,
        error = emailError,
        shape = LoginFieldShape
    )

    Spacer(modifier = Modifier.height(14.dp))

    KocKitPrimaryButton(
        text = "Devam Et",
        onClick = onContinue,
        enabled = email.isNotBlank(),
        isLoading = isLoading,
        containerColor = PastelGreen
    )

    Spacer(modifier = Modifier.height(18.dp))

    ForgotPasswordBackLink(onClick = onBack)
}

@Composable
private fun ForgotPasswordCodeStep(
    email: String,
    code: String,
    codeError: String?,
    isLoading: Boolean,
    onCodeChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitBoldText(
        text = "Doğrulama Kodu",
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    KocKitText(
        text = "$email adresine gönderilen 6 haneli kodu gir.",
        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
        lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(32.dp))

    KocKitOtpCodeField(
        value = code,
        onValueChange = onCodeChange,
        error = codeError
    )

    Spacer(modifier = Modifier.height(14.dp))

    KocKitPrimaryButton(
        text = "Tamam",
        onClick = onContinue,
        enabled = code.length == 6,
        isLoading = isLoading,
        containerColor = PastelGreen
    )

    Spacer(modifier = Modifier.height(18.dp))

    ForgotPasswordBackLink(onClick = onBack)
}

@Composable
private fun ForgotPasswordNewPasswordStep(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    isNewPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    isLoading: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNewPasswordVisibilityToggle: () -> Unit,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitBoldText(
        text = "Yeni Şifre",
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(8.dp))

    KocKitText(
        text = "Hesabın için yeni bir şifre belirle.",
        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
        lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(32.dp))

    KocKitPasswordField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        placeholder = "Yeni şifre",
        isPasswordVisible = isNewPasswordVisible,
        onPasswordVisibilityToggle = onNewPasswordVisibilityToggle,
        error = newPasswordError,
        shape = LoginFieldShape
    )

    Spacer(modifier = Modifier.height(12.dp))

    KocKitPasswordField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        placeholder = "Yeni şifre tekrar",
        isPasswordVisible = isConfirmPasswordVisible,
        onPasswordVisibilityToggle = onConfirmPasswordVisibilityToggle,
        error = confirmPasswordError,
        shape = LoginFieldShape
    )

    Spacer(modifier = Modifier.height(14.dp))

    KocKitPrimaryButton(
        text = "Devam Et",
        onClick = onContinue,
        enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
        isLoading = isLoading,
        containerColor = PastelGreen
    )

    Spacer(modifier = Modifier.height(18.dp))

    ForgotPasswordBackLink(onClick = onBack)
}

@Composable
private fun ForgotPasswordBackLink(onClick: () -> Unit) {
    val colors = KocKitTheme.extraColors

    KocKitText(
        text = "Geri dön",
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        style = MaterialTheme.typography.bodyLarge,
        color = colors.primaryTeal,
        textAlign = TextAlign.Center
    )
}
