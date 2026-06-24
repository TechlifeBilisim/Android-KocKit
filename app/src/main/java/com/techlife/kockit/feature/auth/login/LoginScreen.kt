package com.techlife.kockit.feature.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
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
        onEmailChanged = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
        onPasswordChanged = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
        onPasswordVisibilityToggle = { viewModel.onEvent(LoginEvent.PasswordVisibilityChanged) },
        onForgotPasswordClick = { viewModel.onEvent(LoginEvent.ForgotPasswordClicked) },
        onLoginClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
        onGoogleLoginClick = { viewModel.onEvent(LoginEvent.GoogleLoginClicked) },
        onRegisterClick = { viewModel.onEvent(LoginEvent.RegisterClicked) }
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    KocKitBackground(useFormBackgroundImage = true) {
        AuthFormContainer(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
        ) { metrics ->
            LoginFormBody(
                uiState = uiState,
                metrics = metrics,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onPasswordVisibilityToggle = onPasswordVisibilityToggle,
                onForgotPasswordClick = onForgotPasswordClick,
                onLoginClick = onLoginClick,
                onGoogleLoginClick = onGoogleLoginClick,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
private fun ColumnScope.LoginFormBody(
    uiState: LoginUiState,
    metrics: AuthFormMetrics,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val colors = KocKitTheme.extraColors
    val loginEnabled = uiState.email.isNotBlank() && uiState.password.isNotBlank()

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

    KocKitTextField(
        value = uiState.email,
        onValueChange = onEmailChanged,
        placeholder = "E-posta adresin",
        leadingIconVector = Icons.Filled.Email,
        error = uiState.emailError,
        shape = LoginFieldShape,
        fieldHeight = metrics.fieldHeight,
        textFontSize = metrics.fieldFontSize,
        textLineHeight = metrics.fieldLineHeight
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    KocKitPasswordField(
        value = uiState.password,
        onValueChange = onPasswordChanged,
        placeholder = "Şifren",
        isPasswordVisible = uiState.isPasswordVisible,
        onPasswordVisibilityToggle = onPasswordVisibilityToggle,
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
            .clickable { onForgotPasswordClick() },
        color = colors.textPrimary
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    KocKitPrimaryButton(
        text = "Giriş Yap",
        onClick = onLoginClick,
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
        onClick = onGoogleLoginClick,
        iconPainter = painterResource(R.drawable.ic_google),
        height = metrics.socialButtonHeight,
        fontSize = metrics.bodyFontSize,
        iconSize = if (metrics.isExpanded) 26.dp else 22.dp
    )

    Spacer(modifier = Modifier.height(metrics.fieldSpacing))

    LoginRegisterFooter(
        metrics = metrics,
        onRegisterClick = onRegisterClick,
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

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
private fun LoginScreenTabletPreview() {
    KocKitTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onPasswordVisibilityToggle = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onGoogleLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    KocKitTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onPasswordVisibilityToggle = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onGoogleLoginClick = {},
            onRegisterClick = {}
        )
    }
}
