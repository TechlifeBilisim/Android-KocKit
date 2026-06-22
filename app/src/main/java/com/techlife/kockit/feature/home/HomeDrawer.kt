package com.techlife.kockit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

private val drawerItems = listOf(
    "Profil",
    "Hedeflerim",
    "Ayarlar",
    "Yardım",
    "Çıkış Yap"
)

@Composable
fun HomeDrawerContent(
    userName: String,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(280.dp),
        drawerContainerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(CreamBackground)
                .padding(24.dp)
        ) {
            KocKitBoldText(
                text = "Merhaba, $userName",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
            Spacer(modifier = Modifier.height(4.dp))
            KocKitText(
                text = "KoçKit menüsü",
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeBody,
                lineHeight = KocKitTextDefaults.lineHeightBody
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = TextSecondary.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))
            drawerItems.forEach { item ->
                KocKitSemiText(
                    text = item,
                    color = if (item == "Çıkış Yap") OrangeAccent else TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeDrawerContentPreview() {
    KocKitTheme {
        HomeDrawerContent(
            userName = HomeFakeData.USER_NAME
        )
    }
}
