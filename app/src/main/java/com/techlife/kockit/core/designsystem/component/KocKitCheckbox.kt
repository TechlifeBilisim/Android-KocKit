package com.techlife.kockit.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.White

@Composable
fun KocKitCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 22.dp,
    cornerRadius: Dp = 8.dp,
) {
    val colors = KocKitTheme.extraColors

    val targetBorderWidth = if (checked) 0.dp else 1.5.dp
    val borderWidth by animateDpAsState(targetValue = targetBorderWidth, label = "checkboxBorder")
    val scale by animateFloatAsState(targetValue = if (checked) 1f else 0.98f, label = "checkboxScale")

    val backgroundColor = if (!enabled) {
        colors.borderLight.copy(alpha = 0.4f)
    } else if (checked) {
        colors.pastelGreen
    } else {
        Color.Transparent
    }

    val borderColor = if (!enabled) {
        colors.borderLight.copy(alpha = 0.7f)
    } else {
        colors.borderLight
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = fadeIn() + scaleIn(initialScale = 0.6f),
            exit = fadeOut() + scaleOut(targetScale = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitCheckboxPreview() {
    KocKitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            KocKitCheckbox(
                checked = false,
                onCheckedChange = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            KocKitCheckbox(
                checked = true,
                onCheckedChange = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            KocKitCheckbox(
                checked = false,
                onCheckedChange = {},
                enabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            KocKitCheckbox(
                checked = true,
                onCheckedChange = {},
                enabled = false
            )
        }
    }
}

