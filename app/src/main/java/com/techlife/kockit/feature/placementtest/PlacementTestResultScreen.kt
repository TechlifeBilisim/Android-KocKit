package com.techlife.kockit.feature.placementtest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary

@Composable
fun PlacementTestResultScreen(
    section: PlacementTestSection,
    onBackClick: () -> Unit,
    onContinue: () -> Unit
) {
    PlacementTestDecorBackground(accentSoftColor = section.accentSoftColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlacementTestExamHeader(
                    title = section.examTitle,
                    onBackClick = onBackClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(section.trophyIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                KocKitExtraBoldText(
                    text = section.resultTitle,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeTitle,
                    lineHeight = KocKitTextDefaults.lineHeightTitle,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                val ringColor = if (section == PlacementTestSection.GENERAL_ABILITY) {
                    PlacementTestColors.green
                } else {
                    section.accentColor
                }
                PlacementResultRing(
                    scoreText = section.netScore,
                    label = section.netLabel,
                    progress = section.progress,
                    ringColor = ringColor
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PlacementResultStat(
                        label = "Doğru",
                        value = section.correctCount,
                        valueColor = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    PlacementResultStat(
                        label = "Yanlış",
                        value = section.wrongCount,
                        valueColor = PlacementTestColors.wrongRed,
                        modifier = Modifier.weight(1f)
                    )
                    PlacementResultStat(
                        label = "Boş",
                        value = section.emptyCount,
                        valueColor = PlacementTestColors.emptyBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            KocKitPrimaryButton(
                text = section.resultButtonText,
                onClick = onContinue,
                containerColor = PastelGreen,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            )
        }
    }
}
