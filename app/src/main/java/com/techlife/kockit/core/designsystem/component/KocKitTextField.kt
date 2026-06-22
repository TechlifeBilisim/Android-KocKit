package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextFieldShape

@Composable
fun KocKitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIconVector: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = TextFieldShape,
    fieldHeight: androidx.compose.ui.unit.Dp = 56.dp
) {
    val colors = KocKitTheme.extraColors
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight),
            placeholder = {
                KocKitSemiText(
                    text = placeholder,
                    color = colors.textSecondary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
            },
            leadingIcon = leadingIconVector?.let { vector ->
                {
                    Icon(
                        imageVector = vector,
                        contentDescription = null,
                        tint = colors.textSecondary
                    )
                }
            },
            trailingIcon = trailingIcon,
            isError = error != null,
            shape = shape,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = KocKitFontFamily,
                color = colors.textPrimary
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.cardBackground,
                unfocusedContainerColor = colors.cardBackground,
                disabledContainerColor = colors.cardBackground,
                focusedBorderColor = colors.pastelGreen,
                unfocusedBorderColor = colors.borderLight,
                disabledBorderColor = colors.borderLight,
                errorBorderColor = colors.coralAccent,
                focusedLeadingIconColor = colors.textSecondary,
                unfocusedLeadingIconColor = colors.textSecondary,
                cursorColor = colors.pastelGreen
            ),
            singleLine = true
        )
        error?.let {
            KocKitSemiText(
                text = it,
                color = colors.errorAccent,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

val LoginFieldShape = RoundedCornerShape(12.dp)

@Preview(showBackground = true)
@Composable
private fun KocKitTextFieldPreview() {
    KocKitTheme {
        KocKitTextField(
            value = "",
            onValueChange = {},
            placeholder = "Email Address",
            leadingIconVector = Icons.Filled.Email
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitTextFieldErrorPreview() {
    KocKitTheme {
        KocKitTextField(
            value = "invalid email",
            onValueChange = {},
            placeholder = "Email Address",
            leadingIconVector = Icons.Filled.Email,
            error = "Please enter a valid email address"
        )
    }
}
