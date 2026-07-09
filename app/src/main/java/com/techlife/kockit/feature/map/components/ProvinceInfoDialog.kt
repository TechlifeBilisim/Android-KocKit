package com.techlife.kockit.feature.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.feature.map.model.TurkeyProvince

@Composable
fun ProvinceInfoDialog(
    province: TurkeyProvince?,
    onDismiss: () -> Unit
) {
    if (province == null) return

    val colors = KocKitTheme.extraColors

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            KocKitBoldText(
                text = province.name,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
        },
        text = {
            Column {
                ProvinceInfoRow(label = "Plaka Kodu", value = province.plateCode)
                Spacer(modifier = Modifier.height(8.dp))
                ProvinceInfoRow(label = "Alan Kodu", value = province.areaCode)
                Spacer(modifier = Modifier.height(8.dp))
                ProvinceInfoRow(label = "İl ID", value = province.id)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                KocKitSemiText(
                    text = "Kapat",
                    color = colors.primaryTeal,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
            }
        },
        containerColor = colors.cardBackground
    )
}

@Composable
private fun ProvinceInfoRow(
    label: String,
    value: String
) {
    KocKitSemiText(
        text = "$label: $value",
        color = TextSecondary,
        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
    )
}
