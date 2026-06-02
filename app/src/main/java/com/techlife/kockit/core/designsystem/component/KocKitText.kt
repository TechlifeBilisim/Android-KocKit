package com.techlife.kockit.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily

object KocKitTextDefaults {
    val fontSizeSmall = 12.sp
    val fontSizeBody = 14.sp
    val fontSizeBodyLarge = 16.sp
    val fontSizeSubhead = 15.sp
    val fontSizeTitle = 20.sp
    val fontSizeHeadline = 28.sp
    val fontSizeLogo = 32.sp
    val fontSizeButton = 16.sp

    val lineHeightSmall = 16.sp
    val lineHeightBody = 20.sp
    val lineHeightBodyLarge = 24.sp
    val lineHeightSubhead = 22.sp
    val lineHeightTitle = 28.sp
    val lineHeightHeadline = 36.sp
    val lineHeightLogo = 40.sp
    val lineHeightButton = 24.sp
}

@Composable
fun KocKitText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeBody,
    lineHeight: TextUnit = KocKitTextDefaults.lineHeightBody,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    KocKitStyledText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.Normal,
        fontSize = fontSize,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun KocKitExtraBoldText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeButton,
    lineHeight: TextUnit = KocKitTextDefaults.lineHeightButton,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    KocKitStyledText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun KocKitMediumText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeBody,
    lineHeight: TextUnit = KocKitTextDefaults.lineHeightBody,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    KocKitStyledText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.Medium,
        fontSize = fontSize,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun KocKitSemiText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeBody,
    lineHeight: TextUnit = KocKitTextDefaults.lineHeightBody,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    KocKitStyledText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.SemiBold,
        fontSize = fontSize,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun KocKitBoldText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = KocKitTextDefaults.fontSizeBody,
    lineHeight: TextUnit = KocKitTextDefaults.lineHeightBody,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    KocKitStyledText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        lineHeight = lineHeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
private fun KocKitStyledText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
    color: Color,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    textAlign: TextAlign?,
    maxLines: Int,
    overflow: TextOverflow
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.merge(
            TextStyle(
                fontFamily = KocKitFontFamily,
                fontWeight = fontWeight,
                fontSize = fontSize,
                lineHeight = lineHeight
            )
        ),
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}
