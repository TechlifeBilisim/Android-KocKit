package com.techlife.kockit.core.designsystem.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

data class HomeLayoutMetrics(
    val isExpanded: Boolean,
    val contentMaxWidth: Dp,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val sectionSpacing: Dp,
    val cardInnerPadding: Dp,
    val cardSectionSpacing: Dp,
    val statCardHeight: Dp,
    val greetingTitleSize: TextUnit,
    val greetingTitleLineHeight: TextUnit,
    val greetingSubtitleSize: TextUnit,
    val greetingSubtitleLineHeight: TextUnit,
    val cardTitleSize: TextUnit,
    val cardTitleLineHeight: TextUnit,
    val cardBodySize: TextUnit,
    val cardBodyLineHeight: TextUnit,
    val cardCaptionSize: TextUnit,
    val cardCaptionLineHeight: TextUnit,
    val statCardTitleSize: TextUnit,
    val statCardTitleLineHeight: TextUnit,
    val statCardValueSize: TextUnit,
    val statCardValueLineHeight: TextUnit,
    val statCardLabelSize: TextUnit,
    val statCardLabelLineHeight: TextUnit,
    val statCardFooterSize: TextUnit,
    val statCardFooterLineHeight: TextUnit,
    val progressRingSize: Dp,
    val progressPercentSize: TextUnit,
    val progressPercentLineHeight: TextUnit,
    val menuButtonSize: Dp,
    val menuIconSize: Dp,
    val topBarActionSize: Dp,
    val dailyGoalIconSize: Dp,
    val priorityIconSize: Dp,
    val placementTitleSize: TextUnit,
    val placementTitleLineHeight: TextUnit,
    val placementBodySize: TextUnit,
    val placementBodyLineHeight: TextUnit,
    val bannerImageSize: Dp,
    val useTwoColumnInsights: Boolean
)

val LocalHomeLayoutMetrics = staticCompositionLocalOf { phoneHomeLayoutMetrics() }

@Composable
fun rememberHomeLayoutMetrics(): HomeLayoutMetrics {
    val configuration = LocalConfiguration.current
    val smallestWidth = configuration.smallestScreenWidthDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    return remember(smallestWidth, screenWidth) {
        homeLayoutMetricsForWidth(
            smallestWidth = smallestWidth,
            screenWidth = screenWidth
        )
    }
}

private fun phoneHomeLayoutMetrics() = HomeLayoutMetrics(
    isExpanded = false,
    contentMaxWidth = Dp.Unspecified,
    horizontalPadding = 20.dp,
    topInset = 8.dp,
    sectionSpacing = 16.dp,
    cardInnerPadding = 16.dp,
    cardSectionSpacing = 14.dp,
    statCardHeight = 150.dp,
    greetingTitleSize = KocKitTextDefaults.fontSizeTitle,
    greetingTitleLineHeight = KocKitTextDefaults.lineHeightTitle,
    greetingSubtitleSize = KocKitTextDefaults.fontSizeBodyLarge,
    greetingSubtitleLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    cardTitleSize = KocKitTextDefaults.fontSizeBodyLarge,
    cardTitleLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    cardBodySize = KocKitTextDefaults.fontSizeBody,
    cardBodyLineHeight = KocKitTextDefaults.lineHeightBody,
    cardCaptionSize = KocKitTextDefaults.fontSizeSmall,
    cardCaptionLineHeight = KocKitTextDefaults.lineHeightSmall,
    statCardTitleSize = 9.sp,
    statCardTitleLineHeight = 12.sp,
    statCardValueSize = 22.sp,
    statCardValueLineHeight = 26.sp,
    statCardLabelSize = 10.sp,
    statCardLabelLineHeight = 12.sp,
    statCardFooterSize = 9.sp,
    statCardFooterLineHeight = 12.sp,
    progressRingSize = 64.dp,
    progressPercentSize = 16.sp,
    progressPercentLineHeight = 20.sp,
    menuButtonSize = 44.dp,
    menuIconSize = 22.dp,
    topBarActionSize = 28.dp,
    dailyGoalIconSize = 40.dp,
    priorityIconSize = 42.dp,
    placementTitleSize = 12.sp,
    placementTitleLineHeight = 15.sp,
    placementBodySize = 10.sp,
    placementBodyLineHeight = 13.sp,
    bannerImageSize = 56.dp,
    useTwoColumnInsights = false
)

private fun homeLayoutMetricsForWidth(
    smallestWidth: Dp,
    screenWidth: Dp
): HomeLayoutMetrics {
    val isExpanded = smallestWidth >= 600.dp
    if (!isExpanded) return phoneHomeLayoutMetrics()

    return HomeLayoutMetrics(
        isExpanded = true,
        contentMaxWidth = Dp.Unspecified,
        horizontalPadding = 32.dp,
        topInset = 16.dp,
        sectionSpacing = 20.dp,
        cardInnerPadding = 20.dp,
        cardSectionSpacing = 16.dp,
        statCardHeight = 188.dp,
        greetingTitleSize = 28.sp,
        greetingTitleLineHeight = 34.sp,
        greetingSubtitleSize = 18.sp,
        greetingSubtitleLineHeight = 26.sp,
        cardTitleSize = 20.sp,
        cardTitleLineHeight = 28.sp,
        cardBodySize = 16.sp,
        cardBodyLineHeight = 22.sp,
        cardCaptionSize = 14.sp,
        cardCaptionLineHeight = 18.sp,
        statCardTitleSize = 12.sp,
        statCardTitleLineHeight = 16.sp,
        statCardValueSize = 28.sp,
        statCardValueLineHeight = 32.sp,
        statCardLabelSize = 13.sp,
        statCardLabelLineHeight = 16.sp,
        statCardFooterSize = 12.sp,
        statCardFooterLineHeight = 16.sp,
        progressRingSize = 84.dp,
        progressPercentSize = 20.sp,
        progressPercentLineHeight = 24.sp,
        menuButtonSize = 52.dp,
        menuIconSize = 26.dp,
        topBarActionSize = 32.dp,
        dailyGoalIconSize = 48.dp,
        priorityIconSize = 48.dp,
        placementTitleSize = 16.sp,
        placementTitleLineHeight = 20.sp,
        placementBodySize = 14.sp,
        placementBodyLineHeight = 18.sp,
        bannerImageSize = 68.dp,
        useTwoColumnInsights = screenWidth >= 720.dp
    )
}

@Composable
fun HomeContentContainer(
    modifier: Modifier = Modifier,
    metrics: HomeLayoutMetrics = rememberHomeLayoutMetrics(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalHomeLayoutMetrics provides metrics) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            content()
        }
    }
}
