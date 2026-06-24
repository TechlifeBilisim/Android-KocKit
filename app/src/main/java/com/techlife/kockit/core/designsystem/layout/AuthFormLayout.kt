package com.techlife.kockit.core.designsystem.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults

data class AuthFormMetrics(
    val isExpanded: Boolean,
    val contentMaxWidth: Dp,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val sectionSpacing: Dp,
    val fieldSpacing: Dp,
    val smallSpacing: Dp,
    val headlineFontSize: TextUnit,
    val headlineLineHeight: TextUnit,
    val bodyFontSize: TextUnit,
    val bodyLineHeight: TextUnit,
    val subheadFontSize: TextUnit,
    val subheadLineHeight: TextUnit,
    val fieldHeight: Dp,
    val fieldFontSize: TextUnit,
    val fieldLineHeight: TextUnit,
    val buttonHeight: Dp,
    val buttonFontSize: TextUnit,
    val socialButtonHeight: Dp,
    val logoFontSize: TextUnit,
    val logoLineHeight: TextUnit,
    val logoDotSize: Dp,
    val brandLogoSize: Dp,
    val brandLogoImageSize: Dp,
    val otpCellHeight: Dp,
    val otpDigitFontSize: TextUnit,
    val otpDigitLineHeight: TextUnit
)

@Composable
fun rememberAuthFormMetrics(): AuthFormMetrics {
    val configuration = LocalConfiguration.current
    val smallestWidth = configuration.smallestScreenWidthDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    return remember(smallestWidth, screenWidth) {
        authFormMetricsForWidth(
            smallestWidth = smallestWidth,
            screenWidth = screenWidth
        )
    }
}

private fun authFormMetricsForWidth(
    smallestWidth: Dp,
    screenWidth: Dp
): AuthFormMetrics {
    // Android tablet tanımı: sw600dp+
    val isExpanded = smallestWidth >= 600.dp
    return if (isExpanded) {
        AuthFormMetrics(
            isExpanded = true,
            contentMaxWidth = Dp.Unspecified,
            horizontalPadding = 32.dp,
            topInset = 0.dp,
            sectionSpacing = 24.dp,
            fieldSpacing = 16.dp,
            smallSpacing = 10.dp,
            headlineFontSize = 40.sp,
            headlineLineHeight = 48.sp,
            bodyFontSize = 20.sp,
            bodyLineHeight = 28.sp,
            subheadFontSize = 18.sp,
            subheadLineHeight = 26.sp,
            fieldHeight = 64.dp,
            fieldFontSize = 20.sp,
            fieldLineHeight = 28.sp,
            buttonHeight = 60.dp,
            buttonFontSize = 20.sp,
            socialButtonHeight = 58.dp,
            logoFontSize = 44.sp,
            logoLineHeight = 52.sp,
            logoDotSize = 16.dp,
            brandLogoSize = 120.dp,
            brandLogoImageSize = 88.dp,
            otpCellHeight = 64.dp,
            otpDigitFontSize = 24.sp,
            otpDigitLineHeight = 30.sp
        )
    } else {
        AuthFormMetrics(
            isExpanded = false,
            contentMaxWidth = Dp.Unspecified,
            horizontalPadding = 24.dp,
            topInset = 48.dp,
            sectionSpacing = 32.dp,
            fieldSpacing = 14.dp,
            smallSpacing = 8.dp,
            headlineFontSize = KocKitTextDefaults.fontSizeHeadline,
            headlineLineHeight = KocKitTextDefaults.lineHeightHeadline,
            bodyFontSize = KocKitTextDefaults.fontSizeBodyLarge,
            bodyLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            subheadFontSize = KocKitTextDefaults.fontSizeSubhead,
            subheadLineHeight = KocKitTextDefaults.lineHeightSubhead,
            fieldHeight = 56.dp,
            fieldFontSize = KocKitTextDefaults.fontSizeBodyLarge,
            fieldLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            buttonHeight = 56.dp,
            buttonFontSize = 18.sp,
            socialButtonHeight = 52.dp,
            logoFontSize = KocKitTextDefaults.fontSizeLogo,
            logoLineHeight = KocKitTextDefaults.lineHeightLogo,
            logoDotSize = 12.dp,
            brandLogoSize = 96.dp,
            brandLogoImageSize = 72.dp,
            otpCellHeight = 56.dp,
            otpDigitFontSize = KocKitTextDefaults.fontSizeTitle,
            otpDigitLineHeight = KocKitTextDefaults.lineHeightTitle
        )
    }
}

@Composable
fun AuthFormContainer(
    modifier: Modifier = Modifier,
    metrics: AuthFormMetrics = rememberAuthFormMetrics(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    fillHeight: Boolean = true,
    content: @Composable ColumnScope.(AuthFormMetrics) -> Unit
) {
    val useFullHeight = fillHeight && !metrics.isExpanded
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (useFullHeight) Modifier.fillMaxSize() else Modifier)
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = horizontalAlignment
        ) {
            content(metrics)
        }
    }
}
