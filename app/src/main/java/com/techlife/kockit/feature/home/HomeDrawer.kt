package com.techlife.kockit.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitProfileAvatar
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.ErrorAccent
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LogoBlue
import com.techlife.kockit.core.designsystem.theme.LogoOrange
import com.techlife.kockit.core.designsystem.theme.LogoPink
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

enum class DrawerNavId {
    HOME,
    STUDY_PLAN,
    GOALS,
    ANALYSIS,
    EXAMS,
    NOTIFICATIONS,
    SETTINGS,
    HELP,
    ABOUT,
    LOGOUT
}

private data class DrawerNavItem(
    val id: DrawerNavId,
    val label: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false
)

private val primaryNavItems = listOf(
    DrawerNavItem(DrawerNavId.HOME, "Ana Sayfa", Icons.Filled.Home),
    DrawerNavItem(DrawerNavId.STUDY_PLAN, "Çalışma Planı", Icons.Filled.CalendarToday),
    DrawerNavItem(DrawerNavId.GOALS, "Hedeflerim", Icons.Filled.TrackChanges),
    DrawerNavItem(DrawerNavId.ANALYSIS, "Analizler", Icons.Filled.BarChart),
    DrawerNavItem(DrawerNavId.EXAMS, "Denemeler", Icons.Filled.Description),
    DrawerNavItem(DrawerNavId.NOTIFICATIONS, "Bildirimler", Icons.Filled.Notifications)
)

private val secondaryNavItems = listOf(
    DrawerNavItem(DrawerNavId.SETTINGS, "Ayarlar", Icons.Filled.Settings),
    DrawerNavItem(DrawerNavId.HELP, "Yardım", Icons.Outlined.HelpOutline),
    DrawerNavItem(DrawerNavId.ABOUT, "Hakkımızda", Icons.Filled.Info)
)

@Composable
fun HomeDrawerContent(
    userName: String,
    profileImage: String? = null,
    pointsText: String = "12.350 Puan",
    membershipLabel: String = "Premium Üye",
    selectedItem: DrawerNavId = DrawerNavId.HOME,
    onCloseClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onItemClick: (DrawerNavId) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(304.dp),
        drawerContainerColor = CreamBackground,
        drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(CreamBackground)
        ) {
            DrawerDecor()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                DrawerHeader(
                    onCloseClick = onCloseClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                DrawerProfileCard(
                    userName = userName,
                    profileImage = profileImage,
                    membershipLabel = membershipLabel,
                    pointsText = pointsText,
                    onClick = onProfileClick
                )
                Spacer(modifier = Modifier.height(20.dp))
                primaryNavItems.forEach { item ->
                    DrawerNavRow(
                        item = item,
                        selected = item.id == selectedItem,
                        onClick = { onItemClick(item.id) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = TextSecondary.copy(alpha = 0.12f))
                Spacer(modifier = Modifier.height(10.dp))
                secondaryNavItems.forEach { item ->
                    DrawerNavRow(
                        item = item,
                        selected = false,
                        onClick = { onItemClick(item.id) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = TextSecondary.copy(alpha = 0.12f))
                Spacer(modifier = Modifier.height(10.dp))
                DrawerNavRow(
                    item = DrawerNavItem(
                        id = DrawerNavId.LOGOUT,
                        label = "Çıkış Yap",
                        icon = Icons.AutoMirrored.Filled.Logout,
                        isDestructive = true
                    ),
                    selected = false,
                    onClick = { onItemClick(DrawerNavId.LOGOUT) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DrawerHeader(onCloseClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = LogoBlue, fontWeight = FontWeight.ExtraBold)) { append("Koç") }
                withStyle(SpanStyle(color = LogoOrange, fontWeight = FontWeight.ExtraBold)) { append("Kit") }
            },
            fontSize = 28.sp,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(White)
                .border(1.dp, TextSecondary.copy(alpha = 0.12f), CircleShape)
                .clickable(onClick = onCloseClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Kapat",
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DrawerProfileCard(
    userName: String,
    profileImage: String?,
    membershipLabel: String,
    pointsText: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitProfileAvatar(
                imageSource = profileImage,
                size = 56.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                KocKitBoldText(
                    text = userName,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(OrangeAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = OrangeAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    KocKitSemiText(
                        text = membershipLabel,
                        color = OrangeAccent,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = LogoPink,
                        modifier = Modifier.size(14.dp)
                    )
                    KocKitText(
                        text = pointsText,
                        color = TextSecondary,
                        fontSize = KocKitTextDefaults.fontSizeBody,
                        lineHeight = KocKitTextDefaults.lineHeightBody
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerNavRow(
    item: DrawerNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) OrangeAccent.copy(alpha = 0.18f) else Color.Transparent
    val contentColor = when {
        item.isDestructive -> ErrorAccent
        selected -> OrangeAccent
        else -> TextPrimary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        KocKitSemiText(
            text = item.label,
            color = contentColor,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
    }
}

@Composable
private fun DrawerDecor() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val dotColor = Color(0xFFE8DCC8).copy(alpha = 0.55f)
        val step = 18.dp.toPx()
        for (x in 0..4) {
            for (y in 0..5) {
                drawCircle(
                    color = dotColor,
                    radius = 1.6.dp.toPx(),
                    center = Offset(size.width - 28.dp.toPx() - x * step, 90.dp.toPx() + y * step)
                )
            }
        }
        for (x in 0..5) {
            for (y in 0..4) {
                drawCircle(
                    color = dotColor,
                    radius = 1.6.dp.toPx(),
                    center = Offset(24.dp.toPx() + x * step, size.height - 90.dp.toPx() + y * step)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, bottom = 8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        KocKitExtraBoldText(
            text = "K",
            color = OrangeAccent.copy(alpha = 0.08f),
            fontSize = 160.sp,
            lineHeight = 160.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 780)
@Composable
private fun HomeDrawerContentPreview() {
    KocKitTheme {
        HomeDrawerContent(userName = "Ayşe Geçgel")
    }
}
