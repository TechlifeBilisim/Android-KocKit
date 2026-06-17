package com.techlife.kockit.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.CardShape
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White
import com.techlife.kockit.feature.placementtest.PlacementExamQuestionProgress

private object HomePerformanceCardStyle {
    val green = Color(0xFF107C41)
    val greenLight = Color(0xFFE6F4EA)
    val orange = Color(0xFFD96D00)
    val orangeLight = Color(0xFFFFF4E5)
    val border = Color(0xFFE2E8E5)
    val summaryMuted = Color(0xFF5F6368)
}

@Composable
fun HomeTopBar(
    notificationCount: Int,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(PastelGreen.copy(alpha = 0.18f))
                .clickable(onClick = onMenuClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menü",
                modifier = Modifier.size(22.dp),
                tint = PastelGreen
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Ara",
                modifier = Modifier.size(28.dp),
                tint = TextPrimary
            )
            Box {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Bildirimler",
                    modifier = Modifier.size(28.dp),
                    tint = TextPrimary
                )
                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE85D5D)),
                        contentAlignment = Alignment.Center
                    ) {
                        KocKitText(
                            text = notificationCount.toString(),
                            color = White,
                            fontSize = KocKitTextDefaults.fontSizeSmall,
                            lineHeight = KocKitTextDefaults.lineHeightSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeGreetingSection(
    userName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        KocKitExtraBoldText(
            text = "Merhaba, $userName! 👋",
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeTitle,
            lineHeight = KocKitTextDefaults.lineHeightTitle
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = "Bugün harika bir gün! Planlı adımlarla hedeflerine yaklaş.",
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
    }
}

@Composable
fun HomeDailyGoalCard(
    completedNet: Int,
    totalNet: Int,
    remainingNet: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PastelGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = PastelGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    KocKitText(
                        text = "Bugünkü Hedefin",
                        color = TextSecondary,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                    KocKitBoldText(
                        text = "$completedNet / $totalNet net",
                        color = PastelGreen,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(OrangeAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.TrackChanges,
                        contentDescription = null,
                        tint = OrangeAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    KocKitBoldText(
                        text = "$remainingNet net kaldı",
                        color = PastelGreen,
                        fontSize = KocKitTextDefaults.fontSizeBody,
                        lineHeight = KocKitTextDefaults.lineHeightBody
                    )
                    KocKitText(
                        text = "Devam et, başaracaksın! 🔥",
                        color = TextSecondary,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun HomeStatsCarousel(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeStatCardCompactHeight),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HomeProgressCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                compact = true
            )
            HomePointsCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                compact = true
            )
            HomeExamAverageCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                compact = true
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        HomeStatsPagerDots(selectedPage = 0)
    }
}

@Composable
private fun HomeStatsPagerDots(
    selectedPage: Int,
    pageCount: Int = 3
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val selected = selectedPage == index
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(6.dp)
                    .width(if (selected) 18.dp else 6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (selected) PastelGreen else Color(0xFFD1D5DB)
                    )
            )
        }
    }
}

@Composable
fun HomePerformanceCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, HomePerformanceCardStyle.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(HomePerformanceCardStyle.greenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = null,
                        tint = HomePerformanceCardStyle.green,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                KocKitBoldText(
                    text = "Performans Analizi",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HomePerformanceCardStyle.border),
                    modifier = Modifier.height(32.dp)
                ) {
                    KocKitSemiText(
                        text = "Detaylı Analiz >",
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomePerformanceInsightCell(
                    modifier = Modifier.weight(1f),
                    label = "En Güçlü Ders",
                    subject = "Matematik",
                    percent = "%70",
                    accentColor = HomePerformanceCardStyle.green,
                    accentLightColor = HomePerformanceCardStyle.greenLight,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = HomePerformanceCardStyle.green,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    thickness = 1.dp,
                    color = HomePerformanceCardStyle.border
                )
                HomePerformanceInsightCell(
                    modifier = Modifier.weight(1f),
                    label = "Gelişim Alanı",
                    subject = "Fizik",
                    percent = "%40",
                    accentColor = HomePerformanceCardStyle.orange,
                    accentLightColor = HomePerformanceCardStyle.orangeLight,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.TrackChanges,
                            contentDescription = null,
                            tint = HomePerformanceCardStyle.orange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = HomePerformanceCardStyle.border
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = HomePerformanceCardStyle.green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    KocKitText(
                        text = "Son 7 günde ",
                        color = HomePerformanceCardStyle.summaryMuted,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                    KocKitBoldText(
                        text = "+%6",
                        color = HomePerformanceCardStyle.green,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                    KocKitText(
                        text = " gelişim",
                        color = HomePerformanceCardStyle.summaryMuted,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = HomePerformanceCardStyle.green,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KocKitBoldText(
                            text = "Detaylı Analizi Gör",
                            color = HomePerformanceCardStyle.green,
                            fontSize = KocKitTextDefaults.fontSizeSmall,
                            lineHeight = KocKitTextDefaults.lineHeightSmall
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = HomePerformanceCardStyle.green,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePerformanceInsightCell(
    label: String,
    subject: String,
    percent: String,
    accentColor: Color,
    accentLightColor: Color,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(accentLightColor),
            contentAlignment = Alignment.Center
        ) {
            leadingIcon()
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            KocKitSemiText(
                text = label,
                color = accentColor,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(2.dp))
            KocKitBoldText(
                text = subject,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeBody,
                lineHeight = KocKitTextDefaults.lineHeightBody,
                maxLines = 1
            )
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = accentLightColor
        ) {
            KocKitBoldText(
                text = percent,
                color = accentColor,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

private val HomeStatCardHeight = 220.dp
private val HomeStatCardCompactHeight = 172.dp

@Composable
private fun HomeStatCardShell(
    modifier: Modifier = Modifier,
    headerIcon: ImageVector,
    headerIconBg: Color,
    headerIconTint: Color,
    title: String,
    footerBg: Color,
    footerTint: Color,
    footerIcon: ImageVector,
    footerText: String,
    compact: Boolean = false,
    content: @Composable () -> Unit
) {
    val cardPadding = if (compact) 6.dp else 12.dp
    val headerIconSize = if (compact) 24.dp else 32.dp
    val headerGlyphSize = if (compact) 13.dp else 18.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (compact) HomeStatCardCompactHeight else HomeStatCardHeight),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (compact) 1.dp else 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(headerIconSize)
                        .clip(RoundedCornerShape(8.dp))
                        .background(headerIconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = headerIcon,
                        contentDescription = null,
                        tint = headerIconTint,
                        modifier = Modifier.size(headerGlyphSize)
                    )
                }
                Spacer(modifier = Modifier.width(if (compact) 4.dp else 8.dp))
                KocKitBoldText(
                    text = title,
                    color = TextPrimary,
                    fontSize = if (compact) 9.sp else KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall,
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )
                if (!compact) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = if (compact) 68.dp else 96.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = footerBg
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = if (compact) 4.dp else 8.dp,
                        vertical = if (compact) 4.dp else 6.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (!compact) {
                        Icon(
                            imageVector = footerIcon,
                            contentDescription = null,
                            tint = footerTint,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    KocKitSemiText(
                        text = footerText,
                        color = footerTint,
                        fontSize = if (compact) 8.sp else KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall,
                        textAlign = TextAlign.Center,
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@Composable
fun HomeProgressCard(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    HomeStatCardShell(
        modifier = modifier,
        headerIcon = Icons.Outlined.BarChart,
        headerIconBg = LavenderAccent.copy(alpha = 0.15f),
        headerIconTint = LavenderAccent,
        title = "Genel İlerleme",
        footerBg = LavenderAccent.copy(alpha = 0.12f),
        footerTint = LavenderAccent,
        footerIcon = Icons.AutoMirrored.Filled.TrendingUp,
        footerText = if (compact) "+%8 artış" else "Geçen haftaya göre +%8 artış",
        compact = compact
    ) {
        HomeCircularProgress(
            progress = HomeFakeData.GENERAL_PROGRESS,
            percentText = HomeFakeData.GENERAL_PROGRESS_PERCENT,
            label = "ilerleme",
            ringColor = LavenderAccent,
            ringSize = if (compact) 52.dp else 84.dp,
            compact = compact
        )
    }
}

@Composable
fun HomePointsCard(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    HomeStatCardShell(
        modifier = modifier,
        headerIcon = Icons.Outlined.EmojiEvents,
        headerIconBg = OrangeAccent.copy(alpha = 0.15f),
        headerIconTint = OrangeAccent,
        title = "Toplam Puan",
        footerBg = OrangeAccent.copy(alpha = 0.12f),
        footerTint = OrangeAccent,
        footerIcon = Icons.Outlined.EmojiEvents,
        footerText = "İlk %10 Sıralaman",
        compact = compact
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KocKitExtraBoldText(
                text = HomeFakeData.TOTAL_POINTS,
                color = TextPrimary,
                fontSize = if (compact) KocKitTextDefaults.fontSizeBodyLarge else KocKitTextDefaults.fontSizeTitle,
                lineHeight = if (compact) {
                    KocKitTextDefaults.lineHeightBodyLarge
                } else {
                    KocKitTextDefaults.lineHeightTitle
                }
            )
            KocKitText(
                text = "Toplam Puan",
                color = TextSecondary,
                fontSize = if (compact) 9.sp else KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
        }
    }
}

@Composable
fun HomeExamAverageCard(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    HomeStatCardShell(
        modifier = modifier,
        headerIcon = Icons.Outlined.Description,
        headerIconBg = PastelGreen.copy(alpha = 0.15f),
        headerIconTint = PastelGreen,
        title = "Deneme Ortalaması",
        footerBg = PastelGreen.copy(alpha = 0.12f),
        footerTint = PastelGreen,
        footerIcon = Icons.AutoMirrored.Filled.TrendingUp,
        footerText = "Son 4 deneme +4 net artış",
        compact = compact
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KocKitExtraBoldText(
                text = HomeFakeData.EXAM_AVERAGE_NET,
                color = TextPrimary,
                fontSize = if (compact) KocKitTextDefaults.fontSizeBodyLarge else KocKitTextDefaults.fontSizeTitle,
                lineHeight = if (compact) {
                    KocKitTextDefaults.lineHeightBodyLarge
                } else {
                    KocKitTextDefaults.lineHeightTitle
                }
            )
            KocKitText(
                text = "TYT Ortalama",
                color = TextSecondary,
                fontSize = if (compact) 9.sp else KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
        }
    }
}

@Composable
private fun HomeCircularProgress(
    progress: Float,
    percentText: String,
    label: String,
    ringColor: Color,
    ringSize: Dp = 120.dp,
    compact: Boolean = false
) {
    val strokeWidth = if (compact) 6.dp else 10.dp
    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = ringColor,
            trackColor = Color(0xFFE8E8E8),
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KocKitExtraBoldText(
                text = percentText,
                color = TextPrimary,
                fontSize = if (compact) KocKitTextDefaults.fontSizeBody else KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = if (compact) {
                    KocKitTextDefaults.lineHeightBody
                } else {
                    KocKitTextDefaults.lineHeightBodyLarge
                }
            )
            KocKitText(
                text = label,
                color = TextSecondary,
                fontSize = if (compact) 9.sp else KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
        }
    }
}

@Composable
fun HomePriorityCard(
    lessons: List<HomePriorityLesson>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.TrackChanges,
                    contentDescription = null,
                    tint = Color(0xFFE85D5D),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitBoldText(
                    text = "Bugünkü Önceliğin",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.height(32.dp)
                ) {
                    KocKitSemiText(
                        text = "Planını Gör >",
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            lessons.forEachIndexed { index, lesson ->
                HomePriorityLessonRow(lesson = lesson)
                if (index < lessons.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E7EB)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun HomePriorityLessonRow(lesson: HomePriorityLesson) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(lesson.accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            KocKitBoldText(
                text = lesson.iconEmoji,
                color = lesson.accentColor,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.width(88.dp)) {
            KocKitBoldText(
                text = lesson.title,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
            KocKitText(
                text = lesson.subtitle,
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            PlacementExamQuestionProgress(
                currentQuestionIndex = lesson.completedQuestions - 1,
                totalQuestions = lesson.totalQuestions
            )
            Spacer(modifier = Modifier.height(4.dp))
            KocKitText(
                text = "${lesson.completedQuestions} / ${lesson.totalQuestions} soru",
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Schedule,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            KocKitSemiText(
                text = "${lesson.durationMinutes} dk",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun HomeGoalBannerCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(
            containerColor = OrangeAccent.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.img_target),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    },
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                KocKitBoldText(
                    text = "Büyük hedefler,",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                )
                KocKitBoldText(
                    text = "planlı adımlarla gerçekleşir!",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                )
            }
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OrangeAccent),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                KocKitSemiText(
                    text = "Hedefini Güncelle",
                    color = OrangeAccent,
                    fontSize = 9.sp,
                    lineHeight = KocKitTextDefaults.lineHeightSmall
                )
            }
        }
    }
}
