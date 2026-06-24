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

data class StudyPlanLayoutMetrics(
    val isExpanded: Boolean,
    val horizontalPadding: Dp,
    val topInset: Dp,
    val sectionSpacing: Dp,
    val cardInnerPadding: Dp,
    val headerTitleSize: TextUnit,
    val headerTitleLineHeight: TextUnit,
    val headerSubtitleSize: TextUnit,
    val headerSubtitleLineHeight: TextUnit,
    val sectionTitleSize: TextUnit,
    val sectionTitleLineHeight: TextUnit,
    val rowTitleSize: TextUnit,
    val rowTitleLineHeight: TextUnit,
    val rowSubtitleSize: TextUnit,
    val rowSubtitleLineHeight: TextUnit,
    val microCaptionSize: TextUnit,
    val microCaptionLineHeight: TextUnit,
    val smallCaptionSize: TextUnit,
    val smallCaptionLineHeight: TextUnit,
    val badgeTextSize: TextUnit,
    val badgeTextLineHeight: TextUnit,
    val dayHourPickerSize: TextUnit,
    val dayHourPickerLineHeight: TextUnit,
    val parameterPickerSize: TextUnit,
    val parameterPickerLineHeight: TextUnit,
    val saveButtonHeight: Dp,
    val saveButtonTextSize: TextUnit,
    val saveButtonTextLineHeight: TextUnit,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val iconChipSize: Dp,
    val iconChipIconSize: Dp,
    val dayStepButtonSize: Dp,
    val paramStepButtonSize: Dp,
    val unavailableDayHeight: Dp,
    val parameterStepperWidth: Dp,
    val dropdownWidth: Dp,
    val dayPickerWidth: Dp
)

val LocalStudyPlanLayoutMetrics = staticCompositionLocalOf { phoneStudyPlanLayoutMetrics() }

@Composable
fun rememberStudyPlanLayoutMetrics(): StudyPlanLayoutMetrics {
    val smallestWidth = LocalConfiguration.current.smallestScreenWidthDp.dp
    return remember(smallestWidth) {
        if (smallestWidth >= 600.dp) tabletStudyPlanLayoutMetrics() else phoneStudyPlanLayoutMetrics()
    }
}

private fun phoneStudyPlanLayoutMetrics() = StudyPlanLayoutMetrics(
    isExpanded = false,
    horizontalPadding = 16.dp,
    topInset = 8.dp,
    sectionSpacing = 12.dp,
    cardInnerPadding = 16.dp,
    headerTitleSize = 24.sp,
    headerTitleLineHeight = 28.sp,
    headerSubtitleSize = 13.sp,
    headerSubtitleLineHeight = 18.sp,
    sectionTitleSize = 15.sp,
    sectionTitleLineHeight = 18.sp,
    rowTitleSize = 13.sp,
    rowTitleLineHeight = 16.sp,
    rowSubtitleSize = 11.sp,
    rowSubtitleLineHeight = 14.sp,
    microCaptionSize = 9.sp,
    microCaptionLineHeight = 13.sp,
    smallCaptionSize = 10.sp,
    smallCaptionLineHeight = 12.sp,
    badgeTextSize = 11.sp,
    badgeTextLineHeight = 13.sp,
    dayHourPickerSize = 22.sp,
    dayHourPickerLineHeight = 24.sp,
    parameterPickerSize = 12.sp,
    parameterPickerLineHeight = 14.sp,
    saveButtonHeight = 52.dp,
    saveButtonTextSize = 16.sp,
    saveButtonTextLineHeight = 18.sp,
    backButtonSize = 40.dp,
    backIconSize = 24.dp,
    iconChipSize = 32.dp,
    iconChipIconSize = 16.dp,
    dayStepButtonSize = 22.dp,
    paramStepButtonSize = 28.dp,
    unavailableDayHeight = 44.dp,
    parameterStepperWidth = 118.dp,
    dropdownWidth = 88.dp,
    dayPickerWidth = 40.dp
)

private fun tabletStudyPlanLayoutMetrics() = StudyPlanLayoutMetrics(
    isExpanded = true,
    horizontalPadding = 32.dp,
    topInset = 16.dp,
    sectionSpacing = 16.dp,
    cardInnerPadding = 20.dp,
    headerTitleSize = 30.sp,
    headerTitleLineHeight = 36.sp,
    headerSubtitleSize = 16.sp,
    headerSubtitleLineHeight = 22.sp,
    sectionTitleSize = 18.sp,
    sectionTitleLineHeight = 22.sp,
    rowTitleSize = 16.sp,
    rowTitleLineHeight = 20.sp,
    rowSubtitleSize = 14.sp,
    rowSubtitleLineHeight = 18.sp,
    microCaptionSize = 12.sp,
    microCaptionLineHeight = 16.sp,
    smallCaptionSize = 13.sp,
    smallCaptionLineHeight = 16.sp,
    badgeTextSize = 13.sp,
    badgeTextLineHeight = 16.sp,
    dayHourPickerSize = 26.sp,
    dayHourPickerLineHeight = 30.sp,
    parameterPickerSize = 14.sp,
    parameterPickerLineHeight = 18.sp,
    saveButtonHeight = 58.dp,
    saveButtonTextSize = 18.sp,
    saveButtonTextLineHeight = 22.sp,
    backButtonSize = 48.dp,
    backIconSize = 28.dp,
    iconChipSize = 38.dp,
    iconChipIconSize = 20.dp,
    dayStepButtonSize = 26.dp,
    paramStepButtonSize = 32.dp,
    unavailableDayHeight = 50.dp,
    parameterStepperWidth = 136.dp,
    dropdownWidth = 104.dp,
    dayPickerWidth = 48.dp
)

@Composable
fun StudyPlanContentContainer(
    modifier: Modifier = Modifier,
    metrics: StudyPlanLayoutMetrics = rememberStudyPlanLayoutMetrics(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalStudyPlanLayoutMetrics provides metrics) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            content()
        }
    }
}
