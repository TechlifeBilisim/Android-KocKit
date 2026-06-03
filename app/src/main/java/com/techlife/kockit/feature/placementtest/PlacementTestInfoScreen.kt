package com.techlife.kockit.feature.placementtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary

@Composable
fun PlacementTestInfoScreen(
    section: PlacementTestSection,
    onStartExam: () -> Unit
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                PlacementTestInfoHeader(title = section.infoTitle)

                Spacer(modifier = Modifier.height(20.dp))

                PlacementHeroIcon(
                    iconRes = section.heroIconRes,
                    accentSoftColor = section.accentSoftColor
                )

                KocKitSemiText(
                    text = section.description,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlacementStatCard(
                        label = "Soru Sayısı",
                        value = section.questionCount,
                        modifier = Modifier.weight(1f)
                    )
                    PlacementStatCard(
                        label = "Süre",
                        value = section.duration,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                KocKitBoldText(
                    text = "Kapsam",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeTitle,
                    lineHeight = KocKitTextDefaults.lineHeightTitle
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (section == PlacementTestSection.GENERAL_CULTURE) {
                    section.scopeItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    PlacementScopeItem(text = item)
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    section.scopeItems.forEach { item ->
                        PlacementScopeItem(text = item)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            KocKitPrimaryButton(
                text = section.startButtonText,
                onClick = onStartExam,
                containerColor = section.accentColor,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
            )
        }
    }
}
