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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitLogo
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val colors = KocKitTheme.extraColors
    var email by remember { mutableStateOf("") }
    val sendEnabled = email.isNotBlank()

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

            KocKitBoldText(
                text = "Şifremi Unuttum",
                fontSize = KocKitTextDefaults.fontSizeHeadline,
                lineHeight = KocKitTextDefaults.lineHeightHeadline,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            KocKitText(
                text = "E-posta adresini gir, sana\nşifre yenileme bağlantısı gönderelim.",
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            KocKitTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "E-posta adresin",
                leadingIconVector = Icons.Filled.Email,
                error = null
            )

            Spacer(modifier = Modifier.height(14.dp))

            KocKitPrimaryButton(
                text = "Bağlantı Gönder",
                onClick = { onShowMessage("Şifre sıfırlama yakında eklenecek.") },
                enabled = sendEnabled,
                isLoading = false,
                containerColor = PastelGreen
            )

            Spacer(modifier = Modifier.height(18.dp))

            KocKitText(
                text = "Geri dön",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigateBack() },
                style = MaterialTheme.typography.bodyLarge,
                color = colors.primaryTeal
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

