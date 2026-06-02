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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = KocKitTheme.extraColors

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                RegisterEffect.NavigateToHome -> onNavigateToHome()
                RegisterEffect.NavigateBack -> onNavigateBack()
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
                KocKitBoldText(
                    text = "Kayıt Ol",
                    fontSize = KocKitTextDefaults.fontSizeHeadline,
                    lineHeight = KocKitTextDefaults.lineHeightHeadline
                )
                KocKitText(
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
                    text = "İleri",
                    onClick = { viewModel.onEvent(RegisterEvent.ContinueClicked) },
                    isLoading = uiState.isLoading,
                    showTrailingArrow = true,
                    containerColor = PastelGreen
                )

                if (uiState.currentStep == 1) {
                    KocKitSemiText(
                        text = "Zaten hesabın var mı? Giriş Yap",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onEvent(RegisterEvent.LoginClicked) },
                        color = colors.primaryTeal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
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

    KocKitTextField(
        uiState.fullName,
        { onEvent(RegisterEvent.FullNameChanged(it)) },
        "Ad Soyad",
        error = uiState.fullNameError
    )
    KocKitTextField(
        uiState.email,
        { onEvent(RegisterEvent.EmailChanged(it)) },
        "E-posta adresi",
        error = uiState.emailError
    )
    KocKitTextField(
        uiState.phone,
        { onEvent(RegisterEvent.PhoneChanged(it)) },
        "Telefon Numaran",
        error = uiState.phoneError
    )
    KocKitPasswordField(
        uiState.password,
        { onEvent(RegisterEvent.PasswordChanged(it)) },
        "Şifre",
        uiState.isPasswordVisible,
        { onEvent(RegisterEvent.PasswordVisibilityChanged) },
        error = uiState.passwordError
    )
    KocKitPasswordField(
        uiState.confirmPassword,
        { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
        "Şifren Tekrar",
        uiState.isConfirmPasswordVisible,
        { onEvent(RegisterEvent.ConfirmPasswordVisibilityChanged) },
        error = uiState.confirmPasswordError
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = uiState.isTermsAccepted,
            onCheckedChange = { onEvent(RegisterEvent.TermsCheckedChanged(it)) }
        )
        KocKitText(
            text = "Kullanım koşullarını okudum ve kabul ediyorum.",
            modifier = Modifier.weight(1f)
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
