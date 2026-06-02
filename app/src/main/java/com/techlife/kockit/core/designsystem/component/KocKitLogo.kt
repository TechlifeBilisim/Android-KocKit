package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LogoBlue
import com.techlife.kockit.core.designsystem.theme.LogoOrange
import com.techlife.kockit.core.designsystem.theme.LogoPink

@Composable
fun KocKitLogo(modifier: Modifier = Modifier) {
    val colors = KocKitTheme.extraColors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(LogoBlue)
            )
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(LogoOrange)
            )
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(LogoPink)
            )
        }
        KocKitBoldText(
            text = "koçkit",
            fontSize = KocKitTextDefaults.fontSizeLogo,
            lineHeight = KocKitTextDefaults.lineHeightLogo,
            color = colors.textPrimary
        )
    }
}
