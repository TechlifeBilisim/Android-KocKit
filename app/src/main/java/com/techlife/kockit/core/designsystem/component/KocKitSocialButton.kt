package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

private val LoginSocialButtonShape = RoundedCornerShape(12.dp)

@Composable
fun KocKitSocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    height: Dp = 52.dp,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeBodyLarge,
    iconSize: Dp = 22.dp
) {
    val colors = KocKitTheme.extraColors
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = LoginSocialButtonShape,
        border = BorderStroke(1.dp, colors.borderLight),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = colors.cardBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconPainter?.let { painter ->
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            KocKitBoldText(
                text = text,
                color = colors.textPrimary,
                fontSize = fontSize
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitSocialButtonPreview() {
    KocKitTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KocKitSocialButton(
                text = "Continue with Google",
                iconPainter = painterResource(id = R.drawable.ic_google),
                onClick = {}
            )
            KocKitSocialButton(
                text = "Sign In",
                onClick = {}
            )
        }
    }
}
