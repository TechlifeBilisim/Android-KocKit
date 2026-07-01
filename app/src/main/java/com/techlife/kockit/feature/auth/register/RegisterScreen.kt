package com.techlife.kockit.feature.auth.register

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
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
import com.techlife.kockit.core.designsystem.component.KocKitPasswordField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitCheckbox
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.layout.AuthFormContainer
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.layout.rememberAuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import androidx.compose.material3.Text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitOtpCodeField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.techlife.kockit.feature.auth.common.AuthMethodTabs
import com.techlife.kockit.feature.auth.common.AuthPhoneNumberField
import com.techlife.kockit.feature.auth.common.KVKK_AGREEMENT_TEXT
import com.techlife.kockit.feature.auth.common.LegalAgreementDialog
import com.techlife.kockit.feature.auth.common.TERMS_AGREEMENT_TEXT
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
    val continueButtonText = when (uiState.currentStep) {
        RegisterSteps.ACCOUNT -> "Devam Et"
        RegisterSteps.VERIFICATION -> "Kod Gönder"
        RegisterSteps.OTP -> "Doğrula"
        else -> "Devam Et"
    }

    val metrics = rememberAuthFormMetrics()

    KocKitBackground(useFormBackgroundImage = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .then(
                    if (!metrics.isExpanded) {
                        Modifier.verticalScroll(rememberScrollState())
                    } else {
                        Modifier
                    }
                )
        ) {
            KocKitTopBar(onBackClick = { onEvent(RegisterEvent.BackClicked) })
            Box(
                modifier = if (metrics.isExpanded) {
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                } else {
                    Modifier.fillMaxWidth()
                },
                contentAlignment = if (metrics.isExpanded) Alignment.Center else Alignment.TopCenter
            ) {
                AuthFormContainer(
                    fillHeight = false,
                    metrics = metrics
                ) {
                    RegisterFormBody(
                        uiState = uiState,
                        metrics = metrics,
                        continueButtonText = continueButtonText,
                        onEvent = onEvent,
                        onGoogleClicked = onGoogleClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterFormBody(
    uiState: RegisterUiState,
    metrics: AuthFormMetrics,
    continueButtonText: String,
    onEvent: (RegisterEvent) -> Unit,
    onGoogleClicked: () -> Unit
) {
    val stepTitle = when (uiState.currentStep) {
        RegisterSteps.ACCOUNT -> "Kayıt Ol"
        RegisterSteps.VERIFICATION -> "Hesabını Doğrula"
        RegisterSteps.OTP -> "Doğrulama Kodu"
        else -> "Kayıt Ol"
    }

    if (!metrics.isExpanded) {
        Spacer(modifier = Modifier.height(metrics.topInset))
    }

    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        KocKitExtraBoldText(
            text = stepTitle,
            fontSize = metrics.headlineFontSize,
            lineHeight = metrics.headlineLineHeight,
            color = Color.Black
        )
        KocKitSemiText(
            text = when (uiState.currentStep) {
                RegisterSteps.ACCOUNT -> "Hesabını oluştur ve hedeflerine ulaş."
                RegisterSteps.VERIFICATION -> "Doğrulama kodunu almak için e-posta veya telefonunu gir."
                RegisterSteps.OTP -> "Gönderilen doğrulama kodunu gir."
                else -> "Kaydını tamamla."
            },
            fontSize = metrics.bodyFontSize,
            lineHeight = metrics.bodyLineHeight
        )

        when (uiState.currentStep) {
            RegisterSteps.ACCOUNT -> RegisterStep1Content(uiState, metrics, onEvent)
            RegisterSteps.VERIFICATION -> RegisterVerificationStep(uiState, metrics, onEvent)
            RegisterSteps.OTP -> RegisterOtpStep(uiState, metrics, onEvent)
        }

        Spacer(modifier = Modifier.height(metrics.smallSpacing))

        KocKitPrimaryButton(
            text = continueButtonText,
            onClick = { onEvent(RegisterEvent.ContinueClicked) },
            enabled = !uiState.isLoading,
            isLoading = uiState.isLoading,
            showTrailingArrow = uiState.currentStep == RegisterSteps.ACCOUNT,
            containerColor = PastelGreen,
            height = metrics.buttonHeight,
            fontSize = metrics.buttonFontSize
        )

        if (uiState.currentStep == RegisterSteps.ACCOUNT) {
            Spacer(modifier = Modifier.height(metrics.smallSpacing))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(metrics.socialButtonHeight)
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
                            modifier = Modifier.size(if (metrics.isExpanded) 34.dp else 32.dp)
                        )
                    }
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
    var showKvkkDialog by remember { mutableStateOf(false) }
    val usageAnnotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = "terms", annotation = "terms")
        withStyle(
            SpanStyle(
                color = colors.pastelGreen,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Kullanım koşullarını ve Kvkk şartlarını ")
        }
        pop()
        append("okudum ve kabul ediyorum.")
    }

    val usageKvkkText = buildAnnotatedString {
        pushStringAnnotation(tag = "kvkk", annotation = "kvkk")
        withStyle(
            SpanStyle(
                color = colors.pastelGreen,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("KVKK kapsamında ")
        }
        pop()
        append("verilerimin işlenmesini istiyorum.")
    }

    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        AuthMethodTabs(
            isNicknameSelected = uiState.accountMethod == RegisterAccountMethod.NICKNAME,
            onNicknameSelected = { onEvent(RegisterEvent.AccountMethodChanged(RegisterAccountMethod.NICKNAME)) },
            onPhoneSelected = { onEvent(RegisterEvent.AccountMethodChanged(RegisterAccountMethod.PHONE)) },
            metrics = metrics
        )

        when (uiState.accountMethod) {
            RegisterAccountMethod.NICKNAME -> {
                KocKitTextField(
                    uiState.nickname,
                    { onEvent(RegisterEvent.NicknameChanged(it)) },
                    "Rumuz",
                    error = uiState.nicknameError,
                    leadingIconVector = Icons.Filled.Tag,
                    fieldHeight = metrics.fieldHeight,
                    textFontSize = metrics.fieldFontSize,
                    textLineHeight = metrics.fieldLineHeight
                )
            }
            RegisterAccountMethod.PHONE -> {
                AuthPhoneNumberField(
                    value = uiState.phone,
                    onValueChange = { onEvent(RegisterEvent.PhoneChanged(it)) },
                    placeholder = "5XX XXX XX XX",
                    error = uiState.phoneError,
                    metrics = metrics
                )
            }
        }

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
            "Şifre Tekrar",
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
                .clickable { showTermsDialog = true }
                .padding(top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitCheckbox(
                checked = uiState.isTermsAccepted,
                onCheckedChange = { showTermsDialog = true }
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
                onClick = { showTermsDialog = true }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showKvkkDialog = true }
                .padding(top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitCheckbox(
                checked = uiState.isDataAccepted,
                onCheckedChange = { showKvkkDialog = true }
            )
            Spacer(modifier = Modifier.size(10.dp))
            androidx.compose.foundation.text.ClickableText(
                text = usageKvkkText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = KocKitFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = if (metrics.isExpanded) 16.sp else 14.sp
                ),
                overflow = TextOverflow.Clip,
                maxLines = 2,
                onClick = { showKvkkDialog = true }
            )
        }

        if (showTermsDialog) {
            LegalAgreementDialog(
                title = "Kullanım Koşulları",
                body = TERMS_AGREEMENT_TEXT,
                metrics = metrics,
                onDismiss = { showTermsDialog = false },
                onAccepted = {
                    onEvent(RegisterEvent.TermsDialogAccepted)
                    showTermsDialog = false
                }
            )
        }

        if (showKvkkDialog) {
            LegalAgreementDialog(
                title = "KVKK Aydınlatma Metni",
                body = KVKK_AGREEMENT_TEXT,
                metrics = metrics,
                onDismiss = { showKvkkDialog = false },
                onAccepted = {
                    onEvent(RegisterEvent.DataDialogAccepted)
                    showKvkkDialog = false
                }
            )
        }

        uiState.termsError?.let { KocKitText(text = it, color = colors.coralAccent) }
    }
}

@Composable
private fun RegisterVerificationStep(
    uiState: RegisterUiState,
    metrics: AuthFormMetrics,
    onEvent: (RegisterEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        RegisterVerificationChannelTabs(
            selectedChannel = uiState.verificationChannel,
            onChannelSelected = { onEvent(RegisterEvent.VerificationChannelChanged(it)) },
            metrics = metrics
        )

        when (uiState.verificationChannel) {
            RegisterVerificationChannel.EMAIL -> {
                KocKitTextField(
                    value = uiState.verificationEmail,
                    onValueChange = { onEvent(RegisterEvent.VerificationEmailChanged(it)) },
                    placeholder = "example@gmail.com",
                    error = uiState.verificationEmailError,
                    leadingIconVector = Icons.Filled.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = LoginFieldShape,
                    fieldHeight = metrics.fieldHeight,
                    textFontSize = metrics.fieldFontSize,
                    textLineHeight = metrics.fieldLineHeight
                )
                KocKitText(
                    text = "Doğrulama kodunu e-posta adresine göndereceğiz.",
                    color = TextSecondary,
                    fontSize = metrics.subheadFontSize,
                    lineHeight = metrics.subheadLineHeight
                )
            }
            RegisterVerificationChannel.PHONE -> {
                AuthPhoneNumberField(
                    value = uiState.verificationPhone,
                    onValueChange = { onEvent(RegisterEvent.VerificationPhoneChanged(it)) },
                    placeholder = "5XX XXX XX XX",
                    error = uiState.verificationPhoneError,
                    metrics = metrics
                )
                KocKitText(
                    text = "Doğrulama kodunu Türkiye cep telefonuna SMS ile göndereceğiz.",
                    color = TextSecondary,
                    fontSize = metrics.subheadFontSize,
                    lineHeight = metrics.subheadLineHeight
                )
            }
        }
    }
}

@Composable
private fun RegisterVerificationChannelTabs(
    selectedChannel: RegisterVerificationChannel,
    onChannelSelected: (RegisterVerificationChannel) -> Unit,
    metrics: AuthFormMetrics
) {
    val colors = KocKitTheme.extraColors
    val tabRadius = if (metrics.isExpanded) 16.dp else 14.dp
    val tabItemRadius = if (metrics.isExpanded) 12.dp else 10.dp
    val tabItemPaddingV = if (metrics.isExpanded) 14.dp else 12.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(tabRadius))
            .background(colors.borderLight.copy(alpha = 0.45f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RegisterVerificationChannel.entries.forEach { channel ->
            val isSelected = selectedChannel == channel
            val label = when (channel) {
                RegisterVerificationChannel.EMAIL -> "E-posta"
                RegisterVerificationChannel.PHONE -> "Telefon"
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(tabItemRadius))
                    .background(if (isSelected) PastelGreen else Color.Transparent)
                    .clickable { onChannelSelected(channel) }
                    .padding(vertical = tabItemPaddingV),
                contentAlignment = Alignment.Center
            ) {
                KocKitSemiText(
                    text = label,
                    color = if (isSelected) Color.White else colors.textPrimary,
                    fontSize = metrics.subheadFontSize,
                    lineHeight = metrics.subheadLineHeight
                )
            }
        }
    }
}

@Composable
private fun RegisterOtpStep(
    uiState: RegisterUiState,
    metrics: AuthFormMetrics,
    onEvent: (RegisterEvent) -> Unit
) {
    val subtitle = when (uiState.verificationChannel) {
        RegisterVerificationChannel.EMAIL -> "E-posta adresine gönderilen kodu gir"
        RegisterVerificationChannel.PHONE -> "Telefon numarana gönderilen kodu gir"
    }

    Column(verticalArrangement = Arrangement.spacedBy(metrics.fieldSpacing)) {
        KocKitText(
            text = subtitle,
            color = TextSecondary,
            fontSize = metrics.bodyFontSize,
            lineHeight = metrics.bodyLineHeight
        )

        if (uiState.otpSentTo.isNotBlank()) {
            KocKitSemiText(
                text = uiState.otpSentTo,
                color = TextPrimary,
                fontSize = metrics.subheadFontSize,
                lineHeight = metrics.subheadLineHeight
            )
        }

        KocKitOtpCodeField(
            value = uiState.otpCode,
            onValueChange = { onEvent(RegisterEvent.OtpCodeChanged(it)) },
            error = uiState.otpError,
            cellHeight = metrics.otpCellHeight,
            digitFontSize = metrics.otpDigitFontSize,
            digitLineHeight = metrics.otpDigitLineHeight
        )

        val resendText = if (uiState.resendSecondsRemaining > 0) {
            val minutes = uiState.resendSecondsRemaining / 60
            val seconds = uiState.resendSecondsRemaining % 60
            "Kodu yeniden gönder %d:%02d".format(minutes, seconds)
        } else {
            "Kodu yeniden gönder"
        }

        KocKitText(
            text = resendText,
            color = TextSecondary,
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = uiState.resendSecondsRemaining == 0,
                    onClick = { onEvent(RegisterEvent.ResendOtpClicked) }
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterVerificationStepPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                currentStep = RegisterSteps.VERIFICATION,
                verificationEmail = "adem@kockit.com"
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterOtpStepPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                currentStep = RegisterSteps.OTP,
                verificationChannel = RegisterVerificationChannel.EMAIL,
                otpSentTo = "a***@kockit.com",
                otpCode = "123456"
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
private fun RegisterScreenTabletPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                fullName = "Adem KocKit",
                email = "adem@kockit.com",
                nickname = "ademkockit",
                accountMethod = RegisterAccountMethod.NICKNAME
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet Verification")
@Composable
private fun RegisterVerificationTabletPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                currentStep = RegisterSteps.VERIFICATION,
                verificationEmail = "adem@kockit.com"
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet OTP")
@Composable
private fun RegisterOtpTabletPreview() {
    KocKitTheme {
        RegisterScreenContent(
            uiState = RegisterUiState(
                currentStep = RegisterSteps.OTP,
                verificationChannel = RegisterVerificationChannel.PHONE,
                otpSentTo = "+90 532 *** ** 41",
                otpCode = "123456"
            ),
            onEvent = {},
            onGoogleClicked = {}
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
                nickname = "ademkockit",
                accountMethod = RegisterAccountMethod.NICKNAME
            ),
            onEvent = {},
            onGoogleClicked = {}
        )
    }
}
