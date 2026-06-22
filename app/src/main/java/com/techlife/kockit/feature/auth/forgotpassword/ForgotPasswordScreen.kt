package com.techlife.kockit.feature.auth.forgotpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitOtpCodeField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ForgotPasswordEffect.NavigateBack -> onNavigateBack()
                ForgotPasswordEffect.NavigateToLogin -> onNavigateToLogin()
                is ForgotPasswordEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    ForgotPasswordContent(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
private fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    KocKitBackground(useFormBackgroundImage = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 28.dp)
        ) {
            when (uiState.currentStep) {
                ForgotPasswordSteps.EMAIL -> ForgotPasswordEmailStep(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    isLoading = uiState.isLoading,
                    onEmailChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                    onContinue = { onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToRegister = onNavigateToRegister
                )
                ForgotPasswordSteps.CODE -> ForgotPasswordCodeStep(
                    code = uiState.code,
                    codeError = uiState.codeError,
                    isLoading = uiState.isLoading,
                    resendSecondsRemaining = uiState.resendSecondsRemaining,
                    onCodeChange = { onEvent(ForgotPasswordEvent.CodeChanged(it)) },
                    onContinue = { onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onBack = { onEvent(ForgotPasswordEvent.BackClicked) },
                    onResendCode = { onEvent(ForgotPasswordEvent.ResendCodeClicked) }
                )
                ForgotPasswordSteps.NEW_PASSWORD -> ForgotPasswordNewPasswordStep(
                    newPassword = uiState.newPassword,
                    confirmPassword = uiState.confirmPassword,
                    newPasswordError = uiState.newPasswordError,
                    confirmPasswordError = uiState.confirmPasswordError,
                    isLoading = uiState.isLoading,
                    onNewPasswordChange = { onEvent(ForgotPasswordEvent.NewPasswordChanged(it)) },
                    onConfirmPasswordChange = { onEvent(ForgotPasswordEvent.ConfirmPasswordChanged(it)) },
                    onContinue = { onEvent(ForgotPasswordEvent.ContinueClicked) },
                    onBack = { onEvent(ForgotPasswordEvent.BackClicked) }
                )
            }
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
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        ForgotPasswordLogo()

        Spacer(modifier = Modifier.height(40.dp))

        KocKitBoldText(
            text = "Lütfen e-posta adresinizi girin.",
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        KocKitTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "example@gmail.com",
            error = emailError,
            shape = LoginFieldShape
        )

        Spacer(modifier = Modifier.height(16.dp))

        KocKitSemiText(
            text = "Giriş ekranına dönün",
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToLogin)
        )

        Spacer(modifier = Modifier.height(28.dp))

        KocKitPrimaryButton(
            text = "Gönder",
            onClick = onContinue,
            enabled = email.isNotBlank(),
            isLoading = isLoading,
            containerColor = PastelGreen
        )

        Spacer(modifier = Modifier.weight(1f))

        KocKitText(
            text = "Hesabınız var mı?",
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ForgotPasswordOutlinedButton(
            text = "Kayıt Ol",
            onClick = onNavigateToRegister
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ForgotPasswordCodeStep(
    code: String,
    codeError: String?,
    isLoading: Boolean,
    resendSecondsRemaining: Int,
    onCodeChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    onResendCode: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ForgotPasswordAuthTopBar(
            title = "Doğrulama Kodu",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(32.dp))

        ForgotPasswordLogo()

        Spacer(modifier = Modifier.height(32.dp))

        KocKitSemiText(
            text = "Doğrulama Kodunu Giriniz",
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        KocKitText(
            text = "E-posta adresinize gönderilen kodu girin",
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        KocKitOtpCodeField(
            value = code,
            onValueChange = onCodeChange,
            error = codeError
        )

        Spacer(modifier = Modifier.height(20.dp))

        val resendText = if (resendSecondsRemaining > 0) {
            val minutes = resendSecondsRemaining / 60
            val seconds = resendSecondsRemaining % 60
            "Kodu yeniden gönder %d:%02d".format(minutes, seconds)
        } else {
            "Kodu yeniden gönder"
        }

        KocKitText(
            text = resendText,
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = resendSecondsRemaining == 0,
                    onClick = onResendCode
                )
        )

        Spacer(modifier = Modifier.height(28.dp))

        KocKitPrimaryButton(
            text = "Gönder",
            onClick = onContinue,
            enabled = code.length == 6,
            isLoading = isLoading,
            containerColor = PastelGreen
        )
    }
}

@Composable
private fun ForgotPasswordNewPasswordStep(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    isLoading: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ForgotPasswordAuthTopBar(
            title = "Şifrenizi Yenileyin",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(40.dp))

        ForgotPasswordLabeledField(
            label = "Yeni şifrenizi girin",
            content = {
                KocKitTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    placeholder = "",
                    error = newPasswordError,
                    shape = LoginFieldShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ForgotPasswordLabeledField(
            label = "Şifrenizi tekrar girin",
            content = {
                KocKitTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    placeholder = "",
                    error = confirmPasswordError,
                    shape = LoginFieldShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        KocKitPrimaryButton(
            text = "Gönder",
            onClick = onContinue,
            enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
            isLoading = isLoading,
            containerColor = PastelGreen
        )
    }
}

@Composable
private fun ForgotPasswordLabeledField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        KocKitText(
            text = label,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ForgotPasswordLogo() {
    Surface(
        modifier = Modifier.size(96.dp),
        shape = RoundedCornerShape(20.dp),
        color = White,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_splash),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ForgotPasswordAuthTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp),
            shape = CircleShape,
            color = White,
            shadowElevation = 4.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        KocKitBoldText(
            text = title,
            color = TextPrimary,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ForgotPasswordOutlinedButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, PastelGreen),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White,
            contentColor = PastelGreen
        )
    ) {
        KocKitBoldText(
            text = text,
            color = PastelGreen,
            fontSize = KocKitTextDefaults.fontSizeButton,
            lineHeight = KocKitTextDefaults.lineHeightButton
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordEmailStepPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.EMAIL),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordCodeStepPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.CODE),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordNewPasswordStepPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.NEW_PASSWORD),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}
