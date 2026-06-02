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
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)
