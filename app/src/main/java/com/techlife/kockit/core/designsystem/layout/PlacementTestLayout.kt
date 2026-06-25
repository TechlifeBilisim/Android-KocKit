package com.techlife.kockit.core.designsystem.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults

data class PlacementTestLayoutMetrics(
    val isExpanded: Boolean,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val bottomInset: Dp,
    val sectionSpacing: Dp,
    val contentSpacing: Dp,
    val cardInnerPadding: Dp,
    val cardCornerRadius: Dp,
    val screenTitleSize: TextUnit,
    val screenTitleLineHeight: TextUnit,
    val examTitleSize: TextUnit,
    val examTitleLineHeight: TextUnit,
    val bodyLargeSize: TextUnit,
    val bodyLargeLineHeight: TextUnit,
    val bodySize: TextUnit,
    val bodyLineHeight: TextUnit,
    val smallSize: TextUnit,
    val smallLineHeight: TextUnit,
    val statValueSize: TextUnit,
    val statValueLineHeight: TextUnit,
    val sectionHeadingSize: TextUnit,
    val sectionHeadingLineHeight: TextUnit,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val heroBoxSize: Dp,
    val heroImageSize: Dp,
    val heroCornerRadius: Dp,
    val statCardPaddingV: Dp,
    val scopeIconBoxSize: Dp,
    val scopeIconSize: Dp,
    val progressHeight: Dp,
    val progressSegmentGap: Dp,
    val timerIconSize: Dp,
    val chipPaddingH: Dp,
    val chipPaddingV: Dp,
    val answerOptionPaddingH: Dp,
    val answerOptionPaddingV: Dp,
    val answerOptionCornerRadius: Dp,
    val answerSelectedIndicatorSize: Dp,
    val answerSelectedCheckSize: Dp,
    val primaryButtonHeight: Dp,
    val primaryButtonTextSize: TextUnit,
    val actionIconSize: Dp,
    val resultCardIconSize: Dp,
    val resultBadgeIconSize: Dp,
    val completionIconBoxSize: Dp,
    val completionIconSize: Dp,
    val areaColumnIconSize: Dp,
    val gridGap: Dp,
    val resultCardSpacing: Dp,
    val resultRowPaddingV: Dp,
    val resultAreaPadding: Dp,
    val resultAreaItemSpacing: Dp,
    val resultDividerSpacingV: Dp,
    val decorBlobLarge: Dp,
    val decorBlobSmall: Dp
)

val LocalPlacementTestLayoutMetrics = staticCompositionLocalOf { phonePlacementTestLayoutMetrics() }

@Composable
fun rememberPlacementTestLayoutMetrics(): PlacementTestLayoutMetrics {
    val smallestWidth = LocalConfiguration.current.smallestScreenWidthDp.dp
    return remember(smallestWidth) {
        if (smallestWidth >= 600.dp) tabletPlacementTestLayoutMetrics() else phonePlacementTestLayoutMetrics()
    }
}

private fun phonePlacementTestLayoutMetrics() = PlacementTestLayoutMetrics(
    isExpanded = false,
    horizontalPadding = 24.dp,
    topInset = 16.dp,
    bottomInset = 24.dp,
    sectionSpacing = 24.dp,
    contentSpacing = 20.dp,
    cardInnerPadding = 16.dp,
    cardCornerRadius = 20.dp,
    screenTitleSize = KocKitTextDefaults.fontSizeTitle,
    screenTitleLineHeight = KocKitTextDefaults.lineHeightTitle,
    examTitleSize = KocKitTextDefaults.fontSizeBodyLarge,
    examTitleLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    bodyLargeSize = KocKitTextDefaults.fontSizeBodyLarge,
    bodyLargeLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    bodySize = KocKitTextDefaults.fontSizeBody,
    bodyLineHeight = KocKitTextDefaults.lineHeightBody,
    smallSize = KocKitTextDefaults.fontSizeSmall,
    smallLineHeight = KocKitTextDefaults.lineHeightSmall,
    statValueSize = KocKitTextDefaults.fontSizeTitle,
    statValueLineHeight = KocKitTextDefaults.lineHeightTitle,
    sectionHeadingSize = KocKitTextDefaults.fontSizeTitle,
    sectionHeadingLineHeight = KocKitTextDefaults.lineHeightTitle,
    backButtonSize = 40.dp,
    backIconSize = 20.dp,
    heroBoxSize = 120.dp,
    heroImageSize = 88.dp,
    heroCornerRadius = 36.dp,
    statCardPaddingV = 14.dp,
    scopeIconBoxSize = 22.dp,
    scopeIconSize = 14.dp,
    progressHeight = 6.dp,
    progressSegmentGap = 6.dp,
    timerIconSize = 16.dp,
    chipPaddingH = 12.dp,
    chipPaddingV = 6.dp,
    answerOptionPaddingH = 16.dp,
    answerOptionPaddingV = 14.dp,
    answerOptionCornerRadius = 14.dp,
    answerSelectedIndicatorSize = 24.dp,
    answerSelectedCheckSize = 14.dp,
    primaryButtonHeight = 52.dp,
    primaryButtonTextSize = KocKitTextDefaults.fontSizeBodyLarge,
    actionIconSize = 18.dp,
    resultCardIconSize = 22.dp,
    resultBadgeIconSize = 14.dp,
    completionIconBoxSize = 48.dp,
    completionIconSize = 24.dp,
    areaColumnIconSize = 16.dp,
    gridGap = 10.dp,
    resultCardSpacing = 12.dp,
    resultRowPaddingV = 6.dp,
    resultAreaPadding = 12.dp,
    resultAreaItemSpacing = 6.dp,
    resultDividerSpacingV = 12.dp,
    decorBlobLarge = 220.dp,
    decorBlobSmall = 180.dp
)

private fun tabletPlacementTestLayoutMetrics() = PlacementTestLayoutMetrics(
    isExpanded = true,
    horizontalPadding = 32.dp,
    topInset = 20.dp,
    bottomInset = 28.dp,
    sectionSpacing = 28.dp,
    contentSpacing = 24.dp,
    cardInnerPadding = 20.dp,
    cardCornerRadius = 24.dp,
    screenTitleSize = 26.sp,
    screenTitleLineHeight = 32.sp,
    examTitleSize = 18.sp,
    examTitleLineHeight = 24.sp,
    bodyLargeSize = 18.sp,
    bodyLargeLineHeight = 26.sp,
    bodySize = 16.sp,
    bodyLineHeight = 22.sp,
    smallSize = 14.sp,
    smallLineHeight = 18.sp,
    statValueSize = 24.sp,
    statValueLineHeight = 30.sp,
    sectionHeadingSize = 22.sp,
    sectionHeadingLineHeight = 28.sp,
    backButtonSize = 48.dp,
    backIconSize = 24.dp,
    heroBoxSize = 150.dp,
    heroImageSize = 110.dp,
    heroCornerRadius = 42.dp,
    statCardPaddingV = 18.dp,
    scopeIconBoxSize = 28.dp,
    scopeIconSize = 18.dp,
    progressHeight = 8.dp,
    progressSegmentGap = 8.dp,
    timerIconSize = 20.dp,
    chipPaddingH = 16.dp,
    chipPaddingV = 8.dp,
    answerOptionPaddingH = 20.dp,
    answerOptionPaddingV = 18.dp,
    answerOptionCornerRadius = 16.dp,
    answerSelectedIndicatorSize = 28.dp,
    answerSelectedCheckSize = 16.dp,
    primaryButtonHeight = 58.dp,
    primaryButtonTextSize = 18.sp,
    actionIconSize = 22.dp,
    resultCardIconSize = 26.dp,
    resultBadgeIconSize = 16.dp,
    completionIconBoxSize = 56.dp,
    completionIconSize = 28.dp,
    areaColumnIconSize = 20.dp,
    gridGap = 14.dp,
    resultCardSpacing = 16.dp,
    resultRowPaddingV = 8.dp,
    resultAreaPadding = 16.dp,
    resultAreaItemSpacing = 10.dp,
    resultDividerSpacingV = 14.dp,
    decorBlobLarge = 280.dp,
    decorBlobSmall = 220.dp
)

@Composable
fun PlacementTestContentContainer(
    modifier: Modifier = Modifier,
    metrics: PlacementTestLayoutMetrics = rememberPlacementTestLayoutMetrics(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalPlacementTestLayoutMetrics provides metrics) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            content()
        }
    }
}
