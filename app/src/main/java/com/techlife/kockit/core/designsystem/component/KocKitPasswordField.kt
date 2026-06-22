package com.techlife.kockit.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun KocKitPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    shape: Shape = LoginFieldShape,
    showTrailingIcon: Boolean = true
) {
    val colors = KocKitTheme.extraColors
    KocKitTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        leadingIconVector = Icons.Filled.Lock,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        error = error,
        shape = shape,
        trailingIcon = if (showTrailingIcon) {
            {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = colors.textSecondary
                    )
                }
            }
        } else null
    )
}

@Preview(showBackground = true)
@Composable
private fun KocKitPasswordFieldPreview() {
    KocKitTheme {
        KocKitPasswordField(
            value = "password123",
            onValueChange = {},
            placeholder = "Enter your password",
            isPasswordVisible = false,
            onPasswordVisibilityToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitPasswordFieldVisiblePreview() {
    KocKitTheme {
        KocKitPasswordField(
            value = "password123",
            onValueChange = {},
            placeholder = "Enter your password",
            isPasswordVisible = true,
            onPasswordVisibilityToggle = {}
        )
    }
}
