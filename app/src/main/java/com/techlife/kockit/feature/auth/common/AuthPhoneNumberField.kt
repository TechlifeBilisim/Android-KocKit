package com.techlife.kockit.feature.auth.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.LoginFieldShape
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AuthPhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String?,
    metrics: AuthFormMetrics,
    modifier: Modifier = Modifier
) {
    val tabGap = if (metrics.isExpanded) 12.dp else 10.dp
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(tabGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.height(metrics.fieldHeight),
            shape = RoundedCornerShape(if (metrics.isExpanded) 14.dp else 12.dp),
            color = KocKitTheme.extraColors.cardBackground
        ) {
            Box(
                modifier = Modifier.padding(horizontal = if (metrics.isExpanded) 18.dp else 14.dp),
                contentAlignment = Alignment.Center
            ) {
                KocKitSemiText(
                    text = "+90",
                    color = TextPrimary,
                    fontSize = metrics.fieldFontSize,
                    lineHeight = metrics.fieldLineHeight
                )
            }
        }
        KocKitTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            error = error,
            leadingIconVector = Icons.Filled.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = LoginFieldShape,
            fieldHeight = metrics.fieldHeight,
            textFontSize = metrics.fieldFontSize,
            textLineHeight = metrics.fieldLineHeight,
            modifier = Modifier.weight(1f)
        )
    }
}
