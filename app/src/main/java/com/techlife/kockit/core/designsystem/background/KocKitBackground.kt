package com.techlife.kockit.core.designsystem.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun KocKitBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color = KocKitTheme.extraColors.creamBackground,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        content()
    }
}
