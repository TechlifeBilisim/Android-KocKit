package com.techlife.kockit.feature.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen

@Composable
fun AuthMethodTabs(
    nicknameLabel: String = "Rumuz",
    phoneLabel: String = "Telefon",
    isNicknameSelected: Boolean,
    onNicknameSelected: () -> Unit,
    onPhoneSelected: () -> Unit,
    metrics: AuthFormMetrics,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors
    val tabRadius = if (metrics.isExpanded) 16.dp else 14.dp
    val tabItemRadius = if (metrics.isExpanded) 12.dp else 10.dp
    val tabItemPaddingV = if (metrics.isExpanded) 14.dp else 12.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(tabRadius))
            .background(colors.borderLight.copy(alpha = 0.45f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AuthMethodTabItem(
            label = nicknameLabel,
            isSelected = isNicknameSelected,
            onClick = onNicknameSelected,
            tabItemRadius = tabItemRadius,
            tabItemPaddingV = tabItemPaddingV,
            metrics = metrics
        )
        AuthMethodTabItem(
            label = phoneLabel,
            isSelected = !isNicknameSelected,
            onClick = onPhoneSelected,
            tabItemRadius = tabItemRadius,
            tabItemPaddingV = tabItemPaddingV,
            metrics = metrics
        )
    }
}

@Composable
private fun RowScope.AuthMethodTabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tabItemRadius: androidx.compose.ui.unit.Dp,
    tabItemPaddingV: androidx.compose.ui.unit.Dp,
    metrics: AuthFormMetrics
) {
    val colors = KocKitTheme.extraColors
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(tabItemRadius))
            .background(if (isSelected) PastelGreen else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = tabItemPaddingV),
        contentAlignment = Alignment.Center
    ) {
        KocKitSemiText(
            text = label,
            color = if (isSelected) Color.White else colors.textPrimary,
            fontSize = metrics.subheadFontSize,
            lineHeight = metrics.subheadLineHeight
        )
    }
}
