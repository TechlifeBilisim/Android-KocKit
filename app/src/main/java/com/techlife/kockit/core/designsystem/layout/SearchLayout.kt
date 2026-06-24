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

data class SearchLayoutMetrics(
    val isExpanded: Boolean,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val sectionSpacing: Dp,
    val headerContentGap: Dp,
    val cardInnerPadding: Dp,
    val chipPaddingH: Dp,
    val chipPaddingV: Dp,
    val gridGap: Dp,
    val sectionTitleSize: TextUnit,
    val sectionTitleLineHeight: TextUnit,
    val linkTextSize: TextUnit,
    val linkTextLineHeight: TextUnit,
    val searchFieldHeight: Dp,
    val searchTextSize: TextUnit,
    val searchTextLineHeight: TextUnit,
    val placeholderSize: TextUnit,
    val placeholderLineHeight: TextUnit,
    val chipTextSize: TextUnit,
    val chipTextLineHeight: TextUnit,
    val topicTitleSize: TextUnit,
    val topicTitleLineHeight: TextUnit,
    val topicSubtitleSize: TextUnit,
    val topicSubtitleLineHeight: TextUnit,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val actionIconSize: Dp,
    val searchIconSize: Dp,
    val chipIconSize: Dp,
    val topicIconBoxSize: Dp,
    val topicIconGraphicSize: Dp,
    val topicIconTextSize: TextUnit,
    val topicArrowSize: Dp
)

val LocalSearchLayoutMetrics = staticCompositionLocalOf { phoneSearchLayoutMetrics() }

@Composable
fun rememberSearchLayoutMetrics(): SearchLayoutMetrics {
    val smallestWidth = LocalConfiguration.current.smallestScreenWidthDp.dp
    return remember(smallestWidth) {
        if (smallestWidth >= 600.dp) tabletSearchLayoutMetrics() else phoneSearchLayoutMetrics()
    }
}

private fun phoneSearchLayoutMetrics() = SearchLayoutMetrics(
    isExpanded = false,
    horizontalPadding = 20.dp,
    topInset = 16.dp,
    sectionSpacing = 28.dp,
    headerContentGap = 20.dp,
    cardInnerPadding = 16.dp,
    chipPaddingH = 14.dp,
    chipPaddingV = 12.dp,
    gridGap = 14.dp,
    sectionTitleSize = KocKitTextDefaults.fontSizeBodyLarge,
    sectionTitleLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    linkTextSize = KocKitTextDefaults.fontSizeSmall,
    linkTextLineHeight = KocKitTextDefaults.lineHeightSmall,
    searchFieldHeight = 44.dp,
    searchTextSize = KocKitTextDefaults.fontSizeBody,
    searchTextLineHeight = KocKitTextDefaults.lineHeightBody,
    placeholderSize = 13.sp,
    placeholderLineHeight = 16.sp,
    chipTextSize = 12.sp,
    chipTextLineHeight = 14.sp,
    topicTitleSize = 12.sp,
    topicTitleLineHeight = 14.sp,
    topicSubtitleSize = 9.sp,
    topicSubtitleLineHeight = 10.sp,
    backButtonSize = 40.dp,
    backIconSize = 20.dp,
    actionIconSize = 20.dp,
    searchIconSize = 18.dp,
    chipIconSize = 14.dp,
    topicIconBoxSize = 30.dp,
    topicIconGraphicSize = 14.dp,
    topicIconTextSize = 14.sp,
    topicArrowSize = 14.dp
)

private fun tabletSearchLayoutMetrics() = SearchLayoutMetrics(
    isExpanded = true,
    horizontalPadding = 32.dp,
    topInset = 16.dp,
    sectionSpacing = 24.dp,
    headerContentGap = 22.dp,
    cardInnerPadding = 18.dp,
    chipPaddingH = 16.dp,
    chipPaddingV = 14.dp,
    gridGap = 16.dp,
    sectionTitleSize = 20.sp,
    sectionTitleLineHeight = 28.sp,
    linkTextSize = 14.sp,
    linkTextLineHeight = 18.sp,
    searchFieldHeight = 52.dp,
    searchTextSize = 16.sp,
    searchTextLineHeight = 22.sp,
    placeholderSize = 16.sp,
    placeholderLineHeight = 20.sp,
    chipTextSize = 14.sp,
    chipTextLineHeight = 18.sp,
    topicTitleSize = 15.sp,
    topicTitleLineHeight = 18.sp,
    topicSubtitleSize = 12.sp,
    topicSubtitleLineHeight = 16.sp,
    backButtonSize = 48.dp,
    backIconSize = 24.dp,
    actionIconSize = 24.dp,
    searchIconSize = 22.dp,
    chipIconSize = 16.dp,
    topicIconBoxSize = 38.dp,
    topicIconGraphicSize = 18.dp,
    topicIconTextSize = 18.sp,
    topicArrowSize = 18.dp
)

@Composable
fun SearchContentContainer(
    modifier: Modifier = Modifier,
    metrics: SearchLayoutMetrics = rememberSearchLayoutMetrics(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSearchLayoutMetrics provides metrics) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            content()
        }
    }
}
