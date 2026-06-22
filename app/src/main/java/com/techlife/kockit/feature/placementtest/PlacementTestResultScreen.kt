package com.techlife.kockit.feature.placementtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary

@Composable
fun PlacementTestResultScreen(
    section: PlacementTestSection,
    onGoToNextExam: () -> Unit,
    onGoToHome: () -> Unit
) {
    PlacementTestDecorBackground(accentSoftColor = section.accentSoftColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KocKitExtraBoldText(
                text = section.resultScreenTitle,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            PlacementResultSummaryCard(
                section = section,
                modifier = Modifier.weight(1.4f)
            )

            PlacementCompletionTimeCard(
                completionTime = section.completionTime,
                averageTime = section.averageTime,
                modifier = Modifier.weight(0.75f)
            )

            PlacementResultAreasGrid(
                strongAreas = section.strongAreas,
                weakAreas = section.weakAreas,
                modifier = Modifier.weight(1.4f)
            )

            PlacementResultActionButtons(
                nextExamButtonText = section.nextExamButtonText,
                onGoToNextExam = onGoToNextExam,
                onGoToHome = onGoToHome,
                modifier = Modifier.fillMaxWidth()
            )
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
