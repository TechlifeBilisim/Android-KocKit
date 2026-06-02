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
import androidx.compose.material3.Checkbox
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
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import androidx.compose.material3.Text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
    val colors = KocKitTheme.extraColors
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

    val isStep1Filled =
        uiState.fullName.isNotBlank() &&
            uiState.email.isNotBlank() &&
            uiState.password.isNotBlank() &&
            uiState.confirmPassword.isNotBlank() &&
            uiState.isTermsAccepted

    val continueEnabled = when (uiState.currentStep) {
        1 -> isStep1Filled
        else -> true
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

    KocKitBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            KocKitTopBar(onBackClick = { viewModel.onEvent(RegisterEvent.BackClicked) })
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KocKitExtraBoldText(
                    text = "Kayıt Ol",
                    fontSize = KocKitTextDefaults.fontSizeHeadline,
                    lineHeight = KocKitTextDefaults.lineHeightHeadline,
                    color = Color.Black
                )
                KocKitSemiText(
                    text = when (uiState.currentStep) {
                        1 -> "Hesabını oluştur ve hedeflerine ulaş."
                        2 -> "Hesabını doğrula."
                        else -> "Kaydını tamamla."
                    },
                    style = MaterialTheme.typography.bodyLarge
                )

                when (uiState.currentStep) {
                    1 -> RegisterStep1Content(uiState, viewModel::onEvent)
                    2 -> RegisterStepPlaceholderContent(
                        title = "Doğrula",
                        description = "Doğrulama adımı tasarımı yakında eklenecek."
                    )
                    3 -> RegisterStepPlaceholderContent(
                        title = "Tamamla",
                        description = "Tamamlama adımı tasarımı yakında eklenecek."
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                KocKitPrimaryButton(
                    text = "Devam Et",
                    onClick = { viewModel.onEvent(RegisterEvent.ContinueClicked) },
                    enabled = continueEnabled,
                    isLoading = uiState.isLoading,
                    showTrailingArrow = true,
                    containerColor = PastelGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .clickable { viewModel.onGoogleClicked() }
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
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun RegisterStep1Content(
    uiState: RegisterUiState,
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

    KocKitTextField(
        uiState.fullName,
        { onEvent(RegisterEvent.FullNameChanged(it)) },
        "Ad Soyad",
        error = uiState.fullNameError,
        leadingIconVector = Icons.Filled.Person
    )
    KocKitTextField(
        uiState.email,
        { onEvent(RegisterEvent.EmailChanged(it)) },
        "E-posta adresi",
        error = uiState.emailError,
        leadingIconVector = Icons.Filled.Email
    )
    KocKitPasswordField(
        uiState.password,
        { onEvent(RegisterEvent.PasswordChanged(it)) },
        "Şifre",
        uiState.isPasswordVisible,
        { onEvent(RegisterEvent.PasswordVisibilityChanged) },
        error = uiState.passwordError,
        showTrailingIcon = false
    )
    KocKitPasswordField(
        uiState.confirmPassword,
        { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
        "Şifren Tekrar",
        uiState.isConfirmPasswordVisible,
        { onEvent(RegisterEvent.ConfirmPasswordVisibilityChanged) },
        error = uiState.confirmPasswordError,
        showTrailingIcon = false
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Checkbox sol kenarı input sol kenarıyla hizalı
            .padding(top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = uiState.isTermsAccepted,
            onCheckedChange = { onEvent(RegisterEvent.TermsCheckedChanged(it)) },
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        androidx.compose.foundation.text.ClickableText(
            text = usageAnnotatedText,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = KocKitFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
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
                    containerColor = colors.pastelGreen
                )
            }
        )
    }
    uiState.termsError?.let { KocKitText(text = it, color = colors.coralAccent) }
}

@Composable
private fun RegisterStepPlaceholderContent(
    title: String,
    description: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        KocKitSemiText(
            text = title,
            fontSize = KocKitTextDefaults.fontSizeTitle,
            lineHeight = KocKitTextDefaults.lineHeightTitle
        )
        KocKitText(description, style = MaterialTheme.typography.bodyMedium)
    }
}
