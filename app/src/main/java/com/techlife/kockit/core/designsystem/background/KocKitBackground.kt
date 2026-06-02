package com.techlife.kockit.core.designsystem.background

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun KocKitBackground(
    modifier: Modifier = Modifier,
    useFormBackgroundImage: Boolean = false,
    backgroundColor: Color = KocKitTheme.extraColors.creamBackground,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (!useFormBackgroundImage) Modifier.background(backgroundColor) else Modifier
            )
    ) {
        if (useFormBackgroundImage) {
            Image(
                painter = painterResource(R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        content()
    }
}
