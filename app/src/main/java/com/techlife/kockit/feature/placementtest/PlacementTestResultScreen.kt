package com.techlife.kockit.feature.placementtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.layout.PlacementTestContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberPlacementTestLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary

@Composable
fun PlacementTestResultScreen(
    section: PlacementTestSection,
    onGoToNextExam: () -> Unit,
    onGoToHome: () -> Unit
) {
    val metrics = rememberPlacementTestLayoutMetrics()

    PlacementTestDecorBackground(accentSoftColor = section.accentSoftColor) {
        PlacementTestContentContainer(metrics = metrics) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(top = metrics.topInset, bottom = metrics.bottomInset)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(metrics.resultCardSpacing)
                ) {
                    KocKitExtraBoldText(
                        text = section.resultScreenTitle,
                        color = TextPrimary,
                        fontSize = metrics.screenTitleSize,
                        lineHeight = metrics.screenTitleLineHeight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PlacementResultSummaryCard(
                        section = section,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PlacementCompletionTimeCard(
                        completionTime = section.completionTime,
                        averageTime = section.averageTime,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PlacementResultAreasGrid(
                        strongAreas = section.strongAreas,
                        weakAreas = section.weakAreas,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(metrics.resultCardSpacing))

                PlacementResultActionButtons(
                    nextExamButtonText = section.nextExamButtonText,
                    onGoToNextExam = onGoToNextExam,
                    onGoToHome = onGoToHome,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlacementTestResultScreenPreview() {
    KocKitTheme {
        PlacementTestResultScreen(
            section = PlacementTestSection.GENERAL_ABILITY,
            onGoToNextExam = {},
            onGoToHome = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
private fun PlacementTestResultScreenTabletPreview() {
    KocKitTheme {
        PlacementTestResultScreen(
            section = PlacementTestSection.GENERAL_ABILITY,
            onGoToNextExam = {},
            onGoToHome = {}
        )
    }
}
