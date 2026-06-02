package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.White

private val LoginButtonShape = RoundedCornerShape(12.dp)

@Composable
fun KocKitPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    showTrailingArrow: Boolean = false,
    containerColor: Color? = null
) {
    val colors = KocKitTheme.extraColors
    val buttonColor = containerColor ?: colors.primaryTeal
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = LoginButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = White,
            disabledContainerColor = buttonColor.copy(alpha = 0.5f),
            disabledContentColor = White.copy(alpha = 0.7f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = White,
                strokeWidth = 2.dp
            )
        } else {
            KocKitBoldText(
                text = text,
                color = Color.White
            )
        }
    }
}
