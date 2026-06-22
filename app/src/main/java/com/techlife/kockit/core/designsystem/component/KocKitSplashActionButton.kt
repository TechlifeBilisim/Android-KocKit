package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.Black
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen

private val SplashButtonShape = RoundedCornerShape(50)

@Composable
fun KocKitSplashActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showTrailingArrow: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 6.dp,
                shape = SplashButtonShape,
                ambientColor = Color.Black.copy(alpha = 0.12f),
                spotColor = Color.Black.copy(alpha = 0.18f)
            ),
        shape = SplashButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = PastelGreen.copy(0.8f),
            contentColor = Black
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            KocKitExtraBoldText(
                text = text,
                color = Black,
                fontSize = KocKitTextDefaults.fontSizeBody
            )
            if (showTrailingArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp)
                        .size(20.dp),
                    tint = Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitSplashActionButtonPreview() {
    KocKitTheme {
        KocKitSplashActionButton(
            text = "Hadi Başlayalım",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
