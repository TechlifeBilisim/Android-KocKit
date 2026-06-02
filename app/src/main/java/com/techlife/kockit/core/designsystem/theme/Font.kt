package com.techlife.kockit.core.designsystem.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.techlife.kockit.R

/**
 * Open Sans — `res/font/` altındaki dosya adları:
 * open_sans_light.ttf, open_sans_regular.ttf, open_sans_medium.ttf,
 * open_sans_semibold.ttf, open_sans_bold.ttf
 */
val KocKitFontFamily = FontFamily(
    Font(R.font.opensans_extrabold, FontWeight.ExtraBold),
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold)
)
