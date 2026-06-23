package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun KocKitOtpCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    error: String? = null
) {
    val colors = KocKitTheme.extraColors
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = { input ->
                onValueChange(input.filter(Char::isDigit).take(length))
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            cursorBrush = SolidColor(colors.pastelGreen),
            textStyle = TextStyle(
                fontFamily = KocKitFontFamily,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            ),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(length) { index ->
                        val char = value.getOrNull(index)?.toString().orEmpty()
                        val isFocusedCell = value.length == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.cardBackground)
                                .border(
                                    width = if (error != null) 1.5.dp else 1.dp,
                                    color = when {
                                        error != null -> colors.coralAccent
                                        isFocusedCell -> colors.pastelGreen
                                        else -> colors.borderLight
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            KocKitExtraBoldText(
                                text = char,
                                color = colors.textPrimary,
                                fontSize = KocKitTextDefaults.fontSizeTitle,
                                lineHeight = KocKitTextDefaults.lineHeightTitle
                            )
                        }
                    }
                }
            }
        )
        error?.let {
            KocKitText(
                text = it,
                color = colors.coralAccent,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitOtpCodeFieldPreview() {
    KocKitTheme {
        Column(
            modifier = Modifier
                .background(KocKitTheme.extraColors.creamBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KocKitOtpCodeField(
                value = "",
                onValueChange = {}
            )
            KocKitOtpCodeField(
                value = "123",
                onValueChange = {}
            )
            KocKitOtpCodeField(
                value = "123456",
                onValueChange = {}
            )
            KocKitOtpCodeField(
                value = "010101",
                onValueChange = {},
                error = "Kod hatalı, lütfen tekrar dene."
            )
        }
    }
}
