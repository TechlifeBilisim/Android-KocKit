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

data class ProfileLayoutMetrics(
    val isExpanded: Boolean,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val sectionSpacing: Dp,
    val cardInnerPadding: Dp,
    val cardSectionSpacing: Dp,
    val gridGap: Dp,
    val headerTitleSize: TextUnit,
    val headerTitleLineHeight: TextUnit,
    val headerSubtitleSize: TextUnit,
    val headerSubtitleLineHeight: TextUnit,
    val cardTitleSize: TextUnit,
    val cardTitleLineHeight: TextUnit,
    val cardBodySize: TextUnit,
    val cardBodyLineHeight: TextUnit,
    val cardCaptionSize: TextUnit,
    val cardCaptionLineHeight: TextUnit,
    val microCaptionSize: TextUnit,
    val microCaptionLineHeight: TextUnit,
    val smallCaptionSize: TextUnit,
    val smallCaptionLineHeight: TextUnit,
    val statValueSize: TextUnit,
    val statValueLineHeight: TextUnit,
    val largeStatValueSize: TextUnit,
    val largeStatValueLineHeight: TextUnit,
    val iconChipSize: Dp,
    val iconChipIconSize: Dp,
    val profileAvatarSize: Dp,
    val profileAvatarIconSize: Dp,
    val cameraBadgeSize: Dp,
    val cameraBadgeIconSize: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val progressHeight: Dp,
    val thickProgressHeight: Dp,
    val progressRingSize: Dp,
    val progressRingIconSize: Dp,
    val infoIconSize: Dp,
    val studyDetailCellHeight: Dp
)

val LocalProfileLayoutMetrics = staticCompositionLocalOf { phoneProfileLayoutMetrics() }

@Composable
fun rememberProfileLayoutMetrics(): ProfileLayoutMetrics {
    val configuration = LocalConfiguration.current
    val smallestWidth = configuration.smallestScreenWidthDp.dp
    return remember(smallestWidth) {
        if (smallestWidth >= 600.dp) tabletProfileLayoutMetrics() else phoneProfileLayoutMetrics()
    }
}

private fun phoneProfileLayoutMetrics() = ProfileLayoutMetrics(
    isExpanded = false,
    horizontalPadding = 20.dp,
    topInset = 8.dp,
    sectionSpacing = 16.dp,
    cardInnerPadding = 14.dp,
    cardSectionSpacing = 12.dp,
    gridGap = 10.dp,
    headerTitleSize = 26.sp,
    headerTitleLineHeight = 32.sp,
    headerSubtitleSize = KocKitTextDefaults.fontSizeBody,
    headerSubtitleLineHeight = KocKitTextDefaults.lineHeightBody,
    cardTitleSize = KocKitTextDefaults.fontSizeBodyLarge,
    cardTitleLineHeight = KocKitTextDefaults.lineHeightBodyLarge,
    cardBodySize = KocKitTextDefaults.fontSizeBody,
    cardBodyLineHeight = KocKitTextDefaults.lineHeightBody,
    cardCaptionSize = KocKitTextDefaults.fontSizeSmall,
    cardCaptionLineHeight = KocKitTextDefaults.lineHeightSmall,
    microCaptionSize = 8.sp,
    microCaptionLineHeight = 10.sp,
    smallCaptionSize = 11.sp,
    smallCaptionLineHeight = 14.sp,
    statValueSize = 22.sp,
    statValueLineHeight = 26.sp,
    largeStatValueSize = 28.sp,
    largeStatValueLineHeight = 32.sp,
    iconChipSize = 28.dp,
    iconChipIconSize = 14.dp,
    profileAvatarSize = 72.dp,
    profileAvatarIconSize = 36.dp,
    cameraBadgeSize = 24.dp,
    cameraBadgeIconSize = 12.dp,
    backButtonSize = 40.dp,
    backIconSize = 24.dp,
    progressHeight = 5.dp,
    thickProgressHeight = 6.dp,
    progressRingSize = 64.dp,
    progressRingIconSize = 18.dp,
    infoIconSize = 12.dp,
    studyDetailCellHeight = 56.dp
)

private fun tabletProfileLayoutMetrics() = ProfileLayoutMetrics(
    isExpanded = true,
    horizontalPadding = 32.dp,
    topInset = 16.dp,
    sectionSpacing = 20.dp,
    cardInnerPadding = 18.dp,
    cardSectionSpacing = 16.dp,
    gridGap = 14.dp,
    headerTitleSize = 30.sp,
    headerTitleLineHeight = 36.sp,
    headerSubtitleSize = 18.sp,
    headerSubtitleLineHeight = 26.sp,
    cardTitleSize = 20.sp,
    cardTitleLineHeight = 28.sp,
    cardBodySize = 16.sp,
    cardBodyLineHeight = 22.sp,
    cardCaptionSize = 14.sp,
    cardCaptionLineHeight = 18.sp,
    microCaptionSize = 12.sp,
    microCaptionLineHeight = 16.sp,
    smallCaptionSize = 14.sp,
    smallCaptionLineHeight = 18.sp,
    statValueSize = 28.sp,
    statValueLineHeight = 32.sp,
    largeStatValueSize = 34.sp,
    largeStatValueLineHeight = 38.sp,
    iconChipSize = 34.dp,
    iconChipIconSize = 18.dp,
    profileAvatarSize = 88.dp,
    profileAvatarIconSize = 44.dp,
    cameraBadgeSize = 28.dp,
    cameraBadgeIconSize = 14.dp,
    backButtonSize = 48.dp,
    backIconSize = 28.dp,
    progressHeight = 6.dp,
    thickProgressHeight = 8.dp,
    progressRingSize = 80.dp,
    progressRingIconSize = 22.dp,
    infoIconSize = 16.dp,
    studyDetailCellHeight = 68.dp
)

@Composable
fun ProfileContentContainer(
    modifier: Modifier = Modifier,
    metrics: ProfileLayoutMetrics = rememberProfileLayoutMetrics(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalProfileLayoutMetrics provides metrics) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            content()
        }
    }
}
