package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen

@Composable
fun KocKitStepDots(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    val inactiveColor = Color(0xFFD1D5DB)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val stepNumber = index + 1
            val isActive = stepNumber == currentStep
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(8.dp)
                    .width(if (isActive) 24.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isActive) PastelGreen else inactiveColor)
            )
        }
    }
}
