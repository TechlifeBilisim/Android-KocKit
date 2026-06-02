package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun KocKitStepIndicator(
    currentStep: Int,
    modifier: Modifier = Modifier,
    steps: List<String> = listOf("Bilgiler", "Doğrula", "Tamamla")
) {
    val colors = KocKitTheme.extraColors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val stepNumber = index + 1
            val isActive = stepNumber == currentStep
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isActive) colors.primaryTeal else colors.stepInactive)
                        .width(28.dp)
                        .height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive) {
                        KocKitBoldText(
                            text = stepNumber.toString(),
                            color = colors.cardBackground
                        )
                    } else {
                        KocKitMediumText(
                            text = stepNumber.toString(),
                            color = colors.textSecondary
                        )
                    }
                }
                if (isActive) {
                    KocKitSemiText(
                        text = label,
                        modifier = Modifier.width(72.dp),
                        color = colors.textPrimary
                    )
                } else {
                    KocKitText(
                        text = label,
                        modifier = Modifier.width(72.dp),
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}
