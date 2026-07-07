package com.techlife.kockit.feature.auth.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.component.KocKitBackButton
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitOtpCodeField
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

@Composable
fun AuthVerificationLogo(
    @DrawableRes imageRes: Int = R.drawable.img_splash,
    size: Dp,
    imageSize: Dp
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
                painter = painterResource(imageRes),
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
fun AuthVerificationTopBar(
    title: String,
    onBackClick: () -> Unit,
    metrics: AuthFormMetrics,
    titleFontSize: TextUnit = if (metrics.isExpanded) 20.sp else 18.sp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = metrics.horizontalPadding)
            .height(if (metrics.isExpanded) 56.dp else 48.dp)
    ) {
        KocKitBackButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        )
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
fun ColumnScope.AuthVerificationCodeStep(
    code: String,
    codeError: String?,
    isLoading: Boolean,
    resendSecondsRemaining: Int,
    metrics: AuthFormMetrics,
    subtitle: String,
    onCodeChange: (String) -> Unit,
    onContinue: () -> Unit,
    onResendCode: () -> Unit,
    continueButtonText: String = "Gönder"
) {
    AuthVerificationLogo(
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
        text = subtitle,
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
        text = continueButtonText,
        onClick = onContinue,
        enabled = code.length == 6,
        isLoading = isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )
}

@Composable
fun ColumnScope.AuthVerificationNewPasswordStep(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    isLoading: Boolean,
    metrics: AuthFormMetrics,
    newPasswordLabel: String,
    confirmPasswordLabel: String,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onContinue: () -> Unit,
    continueButtonText: String = "Gönder"
) {
    AuthVerificationLabeledField(
        label = newPasswordLabel,
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

    AuthVerificationLabeledField(
        label = confirmPasswordLabel,
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
        text = continueButtonText,
        onClick = onContinue,
        enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
        isLoading = isLoading,
        containerColor = PastelGreen,
        height = metrics.buttonHeight,
        fontSize = metrics.buttonFontSize
    )
}

@Composable
private fun AuthVerificationLabeledField(
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
