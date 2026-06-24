package com.techlife.kockit.feature.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitPasswordField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitCheckbox
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.layout.AuthFormContainer
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import androidx.compose.material3.Text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.techlife.kockit.core.auth.GoogleSignInHelper
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToGoalSetup: () -> Unit,
    onNavigateBack: () -> Unit,
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
                RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                RegisterEffect.NavigateToGoalSetup -> onNavigateToGoalSetup()
                RegisterEffect.NavigateBack -> onNavigateBack()
                RegisterEffect.LaunchGoogleSignIn -> {
                    val intent = GoogleSignInHelper.client(context).signInIntent
                    googleLauncher.launch(intent)
                }
                is RegisterEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    RegisterScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onGoogleClicked = viewModel::onGoogleClicked
    )
}

@Composable
private fun RegisterScreenContent(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit,
    onGoogleClicked: () -> Unit
) {
    val isStep1Filled =
        uiState.fullName.isNotBlank() &&
            uiState.email.isNotBlank() &&
            uiState.nickname.isNotBlank() &&
            uiState.password.isNotBlank() &&
            uiState.confirmPassword.isNotBlank() &&
            uiState.isTermsAccepted

    val continueEnabled = when (uiState.currentStep) {
        1 -> isStep1Filled
        else -> true
    }

    KocKitBackground(useFormBackgroundImage = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            KocKitTopBar(onBackClick = { onEvent(RegisterEvent.BackClicked) })
            AuthFormContainer(fillHeight = false) { metrics ->
                RegisterFormBody(
                    uiState = uiState,
                    metrics = metrics,
                    continueEnabled = continueEnabled,
                    onEvent = onEvent,
                    onGoogleClicked = onGoogleClicked
                )
            }
        }
    }
}

@Composable
private fun RegisterFormBody(
    uiState: RegisterUiState,
    metrics: AuthFormMetrics,
    continueEnabled: Boolean,
    onEvent: (RegisterEvent) -> Unit,
    onGoogleClicked: () -> Unit
) {
    if (metrics.isExpanded) {
        Spacer(modifier = Modifier.height(40.dp))
    }
    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        KocKitExtraBoldText(
            text = "Kayıt Ol",
            fontSize = metrics.headlineFontSize,
            lineHeight = metrics.headlineLineHeight,
            color = Color.Black
        )
        KocKitSemiText(
            text = when (uiState.currentStep) {
                1 -> "Hesabını oluştur ve hedeflerine ulaş."
                2 -> "Hesabını doğrula."
                else -> "Kaydını tamamla."
            },
            fontSize = metrics.bodyFontSize,
            lineHeight = metrics.bodyLineHeight
        )

        when (uiState.currentStep) {
            1 -> RegisterStep1Content(uiState, metrics, onEvent)
            2 -> RegisterStepPlaceholderContent(
                title = "Doğrula",
                description = "Doğrulama adımı tasarımı yakında eklenecek.",
                metrics = metrics
            )
            3 -> RegisterStepPlaceholderContent(
                title = "Tamamla",
                description = "Tamamlama adımı tasarımı yakında eklenecek.",
                metrics = metrics
            )
        }

        Spacer(modifier = Modifier.height(metrics.smallSpacing))

        KocKitPrimaryButton(
            text = "Devam Et",
            onClick = { onEvent(RegisterEvent.ContinueClicked) },
            enabled = continueEnabled,
            isLoading = uiState.isLoading,
            showTrailingArrow = true,
            containerColor = PastelGreen,
            height = metrics.buttonHeight,
            fontSize = metrics.buttonFontSize
        )

        Spacer(modifier = Modifier.height(metrics.sectionSpacing))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(if (metrics.isExpanded) 56.dp else 52.dp)
                    .clickable { onGoogleClicked() }
                    .clip(CircleShape),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(if (metrics.isExpanded) 24.dp else 22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(metrics.sectionSpacing))
    }
}

@Composable
private fun RegisterStep1Content(
    uiState: RegisterUiState,
    metrics: AuthFormMetrics,
    onEvent: (RegisterEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors
    var showTermsDialog by remember { mutableStateOf(false) }
    val usageAnnotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = "terms", annotation = "terms")
        withStyle(
            SpanStyle(
                color = colors.pastelGreen,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Kullanım koşulları")
        }
        pop()
        append("nı okudum ve kabul ediyorum.")
    }

    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        KocKitTextField(
            uiState.fullName,
            { onEvent(RegisterEvent.FullNameChanged(it)) },
            "Ad Soyad",
            error = uiState.fullNameError,
            leadingIconVector = Icons.Filled.Person,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight
        )
        KocKitTextField(
            uiState.email,
            { onEvent(RegisterEvent.EmailChanged(it)) },
            "E-posta adresi",
            error = uiState.emailError,
            leadingIconVector = Icons.Filled.Email,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight
        )
        KocKitTextField(
            uiState.nickname,
            { onEvent(RegisterEvent.NicknameChanged(it)) },
            "Nickname",
            error = uiState.nicknameError,
            leadingIconVector = Icons.Filled.Tag,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight
        )
        KocKitPasswordField(
            uiState.password,
            { onEvent(RegisterEvent.PasswordChanged(it)) },
            "Şifre",
            uiState.isPasswordVisible,
            { onEvent(RegisterEvent.PasswordVisibilityChanged) },
            error = uiState.passwordError,
            showTrailingIcon = false,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight
        )
        KocKitPasswordField(
            uiState.confirmPassword,
            { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
            "Şifren Tekrar",
            uiState.isConfirmPasswordVisible,
            { onEvent(RegisterEvent.ConfirmPasswordVisibilityChanged) },
            error = uiState.confirmPasswordError,
            showTrailingIcon = false,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitCheckbox(
                checked = uiState.isTermsAccepted,
                onCheckedChange = { onEvent(RegisterEvent.TermsCheckedChanged(it)) }
            )
            Spacer(modifier = Modifier.size(10.dp))
            androidx.compose.foundation.text.ClickableText(
                text = usageAnnotatedText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = KocKitFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = if (metrics.isExpanded) 16.sp else 14.sp
                ),
                overflow = TextOverflow.Clip,
                maxLines = 2,
                onClick = { offset ->
                    val annotation = usageAnnotatedText.getStringAnnotations(
                        tag = "terms",
                        start = offset,
                        end = offset
                    ).firstOrNull()
                    if (annotation != null) showTermsDialog = true
                }
            )
        }

        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                text = { Spacer(modifier = Modifier.height(1.dp)) },
                confirmButton = {
                    KocKitPrimaryButton(
                        text = "Tamam",
                        onClick = { showTermsDialog = false },
                        containerColor = colors.pastelGreen,
                        height = metrics.buttonHeight,
                        fontSize = metrics.buttonFontSize
                    )
                }
            )
        }
        uiState.termsError?.let { KocKitText(text = it, color = colors.coralAccent) }
    }
}

@Composable
private fun RegisterStepPlaceholderContent(
    title: String,
    description: String,
    metrics: AuthFormMetrics
) {
    Column(verticalArrangement = Arrangement.spacedBy(metrics.smallSpacing)) {
        KocKitSemiText(
            text = title,
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight
        )
        KocKitText(
            description,
            fontSize = metrics.bodyFontSize,
            lineHeight = metrics.bodyLineHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                fullName = "Adem KocKit",
                email = "adem@kockit.com",
                nickname = "ademkockit"
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}
