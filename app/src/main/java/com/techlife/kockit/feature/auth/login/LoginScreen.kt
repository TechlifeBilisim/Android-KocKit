package com.techlife.kockit.feature.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.techlife.kockit.R
import com.techlife.kockit.core.auth.GoogleSignInHelper
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitLogo
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitPasswordField
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitSocialButton
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import com.techlife.kockit.core.designsystem.layout.AuthFormContainer
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.feature.auth.common.AuthMethodTabs
import com.techlife.kockit.feature.auth.common.AuthPhoneNumberField
import com.techlife.kockit.feature.auth.common.normalizeTurkishPhone
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToGoalSetup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val data = result.data
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                task.getResult(ApiException::class.java)
                onNavigateToGoalSetup()
            } catch (_: Exception) {
                onShowMessage("Google hesabı seçilemedi.")
            }
        }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LoginEffect.NavigateToRegister -> onNavigateToRegister()
                LoginEffect.NavigateToGoalSetup -> onNavigateToGoalSetup()
                LoginEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                LoginEffect.LaunchGoogleSignIn -> {
                    val intent = GoogleSignInHelper.client(context).signInIntent
                    googleLauncher.launch(intent)
                }
                is LoginEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit
) {
    KocKitBackground(useFormBackgroundImage = true) {
        AuthFormContainer(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
        ) { metrics ->
            LoginFormBody(
                uiState = uiState,
                metrics = metrics,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun ColumnScope.LoginFormBody(
    uiState: LoginUiState,
    metrics: AuthFormMetrics,
    onEvent: (LoginEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors
    val loginEnabled = when (uiState.loginMethod) {
        LoginMethod.NICKNAME -> uiState.nickname.isNotBlank() && uiState.password.isNotBlank()
        LoginMethod.PHONE -> normalizeTurkishPhone(uiState.phone).length == 10
    }

    Spacer(modifier = Modifier.height(metrics.topInset))

    KocKitLogo(
        fontSize = metrics.logoFontSize,
        lineHeight = metrics.logoLineHeight,
        dotSize = metrics.logoDotSize
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    KocKitBoldText(
        text = "Giriş Yap",
        fontSize = metrics.headlineFontSize,
        lineHeight = metrics.headlineLineHeight,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(metrics.smallSpacing))

    KocKitText(
        text = "Hesabına giriş yap ve \nçalışmalarına devam et.",
        fontSize = metrics.bodyFontSize,
        lineHeight = metrics.bodyLineHeight,
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(metrics.sectionSpacing))

    AuthMethodTabs(
        isNicknameSelected = uiState.loginMethod == LoginMethod.NICKNAME,
        onNicknameSelected = { onEvent(LoginEvent.LoginMethodChanged(LoginMethod.NICKNAME)) },
        onPhoneSelected = { onEvent(LoginEvent.LoginMethodChanged(LoginMethod.PHONE)) },
        metrics = metrics
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    when (uiState.loginMethod) {
        LoginMethod.NICKNAME -> {
            KocKitTextField(
                value = uiState.nickname,
                onValueChange = { onEvent(LoginEvent.NicknameChanged(it)) },
                placeholder = "Rumuz",
                leadingIconVector = Icons.Filled.Tag,
                error = uiState.nicknameError,
                shape = LoginFieldShape,
                fieldHeight = metrics.fieldHeight,
                textFontSize = metrics.fieldFontSize,
                textLineHeight = metrics.fieldLineHeight
            )

            Spacer(modifier = Modifier.height(metrics.fieldSpacing))

            KocKitPasswordField(
                value = uiState.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                placeholder = "Şifre",
                isPasswordVisible = uiState.isPasswordVisible,
                onPasswordVisibilityToggle = { onEvent(LoginEvent.PasswordVisibilityChanged) },
                error = uiState.passwordError,
                shape = LoginFieldShape,
                fieldHeight = metrics.fieldHeight,
                textFontSize = metrics.fieldFontSize,
                textLineHeight = metrics.fieldLineHeight
            )

            Spacer(modifier = Modifier.height(metrics.fieldSpacing))

            KocKitBoldText(
                text = "Şifremi unuttum?",
                fontSize = metrics.bodyFontSize,
                lineHeight = metrics.bodyLineHeight,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onEvent(LoginEvent.ForgotPasswordClicked) },
                color = colors.textPrimary
            )
        }
        LoginMethod.PHONE -> {
            AuthPhoneNumberField(
                value = uiState.phone,
                onValueChange = { onEvent(LoginEvent.PhoneChanged(it)) },
                placeholder = "5XX XXX XX XX",
                error = uiState.phoneError,
                metrics = metrics
            )
        }
    }

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    KocKitPrimaryButton(
        text = "Giriş Yap",
        onClick = { onEvent(LoginEvent.LoginClicked) },
        enabled = loginEnabled,
        isLoading = uiState.isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    LoginOrDivider(metrics = metrics)

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    KocKitSocialButton(
        text = "Google ile giriş yap",
        onClick = { onEvent(LoginEvent.GoogleLoginClicked) },
        iconPainter = painterResource(R.drawable.ic_google),
        height = metrics.socialButtonHeight,
        fontSize = metrics.bodyFontSize,
        iconSize = if (metrics.isExpanded) 26.dp else 22.dp
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    LoginRegisterFooter(
        metrics = metrics,
        onRegisterClick = { onEvent(LoginEvent.RegisterClicked) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
private fun LoginOrDivider(metrics: AuthFormMetrics) {
    val colors = KocKitTheme.extraColors
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = colors.borderLight,
            thickness = 1.dp
        )
        KocKitSemiText(
            text = "veya",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight,
            color = colors.textSecondary
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = colors.borderLight,
            thickness = 1.dp
        )
    }
}

@Composable
private fun LoginRegisterFooter(
    metrics: AuthFormMetrics,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        KocKitSemiText(
            text = "Hesabın yok mu? ",
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight,
            color = colors.textPrimary
        )
        KocKitBoldText(
            text = "Kayıt Ol",
            modifier = Modifier.clickable(onClick = onRegisterClick),
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight,
            color = colors.primaryTeal
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet Nickname")
@Composable
private fun LoginScreenTabletNicknamePreview() {
    KocKitTheme {
        LoginContent(
            uiState = LoginUiState(loginMethod = LoginMethod.NICKNAME),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet Phone")
@Composable
private fun LoginScreenTabletPhonePreview() {
    KocKitTheme {
        LoginContent(
            uiState = LoginUiState(loginMethod = LoginMethod.PHONE, phone = "5321234567"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    KocKitTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEvent = {}
        )
    }
}
