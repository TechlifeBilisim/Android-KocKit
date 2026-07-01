package com.techlife.kockit.feature.auth.forgotpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.techlife.kockit.core.designsystem.layout.AuthFormContainer
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.layout.rememberAuthFormMetrics
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
    onShowMessage: (String) -> Unit,
    profileFlow: Boolean = false,
    onCompleted: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(profileFlow) {
        if (profileFlow) {
            viewModel.startProfileFlow()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ForgotPasswordEffect.NavigateBack -> onNavigateBack()
                ForgotPasswordEffect.NavigateToLogin -> onNavigateToLogin()
                ForgotPasswordEffect.Completed -> onCompleted()
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
    val metrics = rememberAuthFormMetrics()
    val showTopBar = uiState.currentStep != ForgotPasswordSteps.EMAIL
    val topBarTitle = when (uiState.currentStep) {
        ForgotPasswordSteps.CODE -> "Doğrulama Kodu"
        ForgotPasswordSteps.NEW_PASSWORD -> "Şifrenizi Yenileyin"
        else -> ""
    }

    KocKitBackground(useFormBackgroundImage = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            if (showTopBar) {
                ForgotPasswordAuthTopBar(
                    title = topBarTitle,
                    onBackClick = { onEvent(ForgotPasswordEvent.BackClicked) },
                    titleFontSize = if (metrics.isExpanded) 20.sp else 18.sp,
                    metrics = metrics
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = if (metrics.isExpanded) Alignment.Center else Alignment.TopCenter
            ) {
                AuthFormContainer(
                    fillHeight = !metrics.isExpanded,
                    metrics = metrics,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { formMetrics ->
                    when (uiState.currentStep) {
                        ForgotPasswordSteps.EMAIL -> ForgotPasswordEmailStep(
                            email = uiState.email,
                            emailError = uiState.emailError,
                            isLoading = uiState.isLoading,
                            metrics = formMetrics,
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
                            metrics = formMetrics,
                            onCodeChange = { onEvent(ForgotPasswordEvent.CodeChanged(it)) },
                            onContinue = { onEvent(ForgotPasswordEvent.ContinueClicked) },
                            onResendCode = { onEvent(ForgotPasswordEvent.ResendCodeClicked) }
                        )
                        ForgotPasswordSteps.NEW_PASSWORD -> ForgotPasswordNewPasswordStep(
                            newPassword = uiState.newPassword,
                            confirmPassword = uiState.confirmPassword,
                            newPasswordError = uiState.newPasswordError,
                            confirmPasswordError = uiState.confirmPasswordError,
                            isLoading = uiState.isLoading,
                            metrics = formMetrics,
                            onNewPasswordChange = { onEvent(ForgotPasswordEvent.NewPasswordChanged(it)) },
                            onConfirmPasswordChange = { onEvent(ForgotPasswordEvent.ConfirmPasswordChanged(it)) },
                            onContinue = { onEvent(ForgotPasswordEvent.ContinueClicked) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ForgotPasswordEmailStep(
    email: String,
    emailError: String?,
    isLoading: Boolean,
    metrics: AuthFormMetrics,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    if (!metrics.isExpanded) {
        Spacer(modifier = Modifier.height(metrics.topInset))
    }

    ForgotPasswordLogo(
        size = metrics.brandLogoSize,
        imageSize = metrics.brandLogoImageSize
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitBoldText(
        text = "Lütfen e-posta adresinizi girin.",
        color = TextPrimary,
        fontSize = metrics.bodyFontSize,
        lineHeight = metrics.bodyLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitTextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = "example@gmail.com",
        error = emailError,
        shape = LoginFieldShape,
        fieldHeight = metrics.fieldHeight,
        textFontSize = metrics.fieldFontSize,
        textLineHeight = metrics.fieldLineHeight
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    KocKitSemiText(
        text = "Giriş ekranına dönün",
        color = TextSecondary,
        fontSize = metrics.subheadFontSize,
        lineHeight = metrics.subheadLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToLogin)
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitPrimaryButton(
        text = "Gönder",
        onClick = onContinue,
        enabled = email.isNotBlank(),
        isLoading = isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )

    if (!metrics.isExpanded) {
        Spacer(modifier = Modifier.weight(1f))
    }

    KocKitText(
        text = "Hesabınız var mı?",
        color = TextSecondary,
        fontSize = metrics.subheadFontSize,
        lineHeight = metrics.subheadLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    ForgotPasswordOutlinedButton(
        text = "Kayıt Ol",
        onClick = onNavigateToRegister,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))
}

@Composable
private fun ColumnScope.ForgotPasswordCodeStep(
    code: String,
    codeError: String?,
    isLoading: Boolean,
    resendSecondsRemaining: Int,
    metrics: AuthFormMetrics,
    onCodeChange: (String) -> Unit,
    onContinue: () -> Unit,
    onResendCode: () -> Unit
) {
    ForgotPasswordLogo(
        size = metrics.brandLogoSize,
        imageSize = metrics.brandLogoImageSize
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitSemiText(
        text = "Doğrulama Kodunu Giriniz",
        color = TextPrimary,
        fontSize = metrics.bodyFontSize,
        lineHeight = metrics.bodyLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(metrics.smallSpacing))

    KocKitText(
        text = "E-posta adresinize gönderilen kodu girin",
        color = TextSecondary,
        fontSize = metrics.subheadFontSize,
        lineHeight = metrics.subheadLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitOtpCodeField(
        value = code,
        onValueChange = onCodeChange,
        error = codeError,
        cellHeight = metrics.otpCellHeight,
        digitFontSize = metrics.otpDigitFontSize,
        digitLineHeight = metrics.otpDigitLineHeight
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

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
        fontSize = metrics.subheadFontSize,
        lineHeight = metrics.subheadLineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = resendSecondsRemaining == 0,
                onClick = onResendCode
            )
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitPrimaryButton(
        text = "Gönder",
        onClick = onContinue,
        enabled = code.length == 6,
        isLoading = isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )
}

@Composable
private fun ColumnScope.ForgotPasswordNewPasswordStep(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    isLoading: Boolean,
    metrics: AuthFormMetrics,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    ForgotPasswordLabeledField(
        label = "Yeni şifrenizi girin",
        metrics = metrics,
        content = {
            KocKitTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                placeholder = "",
                error = newPasswordError,
                shape = LoginFieldShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                fieldHeight = metrics.fieldHeight,
                textFontSize = metrics.fieldFontSize,
                textLineHeight = metrics.fieldLineHeight
            )
        }
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    ForgotPasswordLabeledField(
        label = "Şifrenizi tekrar girin",
        metrics = metrics,
        content = {
            KocKitTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = "",
                error = confirmPasswordError,
                shape = LoginFieldShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                fieldHeight = metrics.fieldHeight,
                textFontSize = metrics.fieldFontSize,
                textLineHeight = metrics.fieldLineHeight
            )
        }
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitPrimaryButton(
        text = "Gönder",
        onClick = onContinue,
        enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
        isLoading = isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )
}

@Composable
private fun ForgotPasswordLabeledField(
    label: String,
    metrics: AuthFormMetrics,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        KocKitText(
            text = label,
            color = TextPrimary,
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight
        )
        Spacer(modifier = Modifier.height(metrics.smallSpacing))
        content()
    }
}

@Composable
private fun ForgotPasswordLogo(
    size: androidx.compose.ui.unit.Dp = 96.dp,
    imageSize: androidx.compose.ui.unit.Dp = 72.dp
) {
    Surface(
        modifier = Modifier.size(size),
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
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ForgotPasswordAuthTopBar(
    title: String,
    onBackClick: () -> Unit,
    titleFontSize: androidx.compose.ui.unit.TextUnit = 18.sp,
    metrics: AuthFormMetrics
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = metrics.horizontalPadding)
            .height(if (metrics.isExpanded) 56.dp else 48.dp)
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
            fontSize = titleFontSize,
            lineHeight = titleFontSize * 1.2f,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ForgotPasswordOutlinedButton(
    text: String,
    onClick: () -> Unit,
    height: androidx.compose.ui.unit.Dp = 52.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = KocKitTextDefaults.fontSizeButton
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
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
            fontSize = fontSize,
            lineHeight = fontSize * 1.2f
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

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet Email")
@Composable
fun ForgotPasswordEmailTabletPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.EMAIL),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet Code")
@Composable
fun ForgotPasswordCodeTabletPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.CODE),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet New Password")
@Composable
fun ForgotPasswordNewPasswordTabletPreview() {
    KocKitTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(currentStep = ForgotPasswordSteps.NEW_PASSWORD),
            onEvent = {},
            onNavigateToLogin = {},
            onNavigateToRegister = {}
        )
    }
}
