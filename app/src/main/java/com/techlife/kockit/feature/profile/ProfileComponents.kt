package com.techlife.kockit.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

private object ProfileCardStyle {
    val innerPadding = 14.dp
    val sectionSpacing = 12.dp
    val gridGap = 10.dp
    val iconChipSize = 28.dp
    val iconChipRadius = 8.dp
    val progressHeight = 5.dp
    val thickProgressHeight = 6.dp
    val profileAvatarSize = 72.dp
    val levelBadgeGreen = Color(0xFF4DB6AC)
    val levelBadgeBg = Color(0xFFE8F6F3)
    val orangeLight = Color(0xFFFFF0E6)
    val purpleLight = Color(0xFFF0ECFA)
    val purpleGradientStart = Color(0xFF9B8FD9)
    val purpleGradientEnd = Color(0xFFD4CCF0)
    val unavailableChipBg = Color(0xFFFFF5EE)
    val dividerColor = Color(0xFFF0F0F0)
    val atomTint = Color(0xFFE8EEF5)
}

@Composable
fun ProfileTopBar(
    notificationCount: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                modifier = Modifier.size(24.dp),
                tint = TextPrimary
            )
        }
        Box {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Bildirimler",
                modifier = Modifier.size(26.dp),
                tint = TextPrimary
            )
            if (notificationCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE85D5D)),
                    contentAlignment = Alignment.Center
                ) {
                    KocKitText(
                        text = notificationCount.toString(),
                        color = White,
                        fontSize = 9.sp,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        KocKitExtraBoldText(
            text = "Profilim",
            color = TextPrimary,
            fontSize = 26.sp,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = "Hedeflerine ulaşmak için bilgilerini güncel tut.",
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody
        )
    }
}

@Composable
fun ProfileSummaryCard(
    fullName: String,
    grade: String,
    examType: String,
    location: String,
    school: String,
    levelLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Outlined.Science,
                contentDescription = null,
                tint = ProfileCardStyle.atomTint,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
                    .size(56.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ProfileCardStyle.innerPadding)
                    .padding(bottom = 56.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(ProfileCardStyle.profileAvatarSize)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F0FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Color(0xFF90A4C8),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PastelGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Fotoğraf düzenle",
                            tint = White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    KocKitBoldText(
                        text = fullName,
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.School, grade)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.Description, examType)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.LocationOn, location)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.AccountBalance, school)
                }
            }
            ProfileLevelBadge(
                levelLabel = levelLabel,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = ProfileCardStyle.innerPadding,
                        bottom = ProfileCardStyle.innerPadding
                    )
            )
        }
    }
}

@Composable
private fun ProfileLevelBadge(
    levelLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, ProfileCardStyle.levelBadgeBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KocKitText(
                text = "Düzey",
                color = TextSecondary,
                fontSize = 9.sp,
                lineHeight = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ProfileCardStyle.levelBadgeBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = ProfileCardStyle.levelBadgeGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    KocKitSemiText(
                        text = levelLabel,
                        color = ProfileCardStyle.levelBadgeGreen,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSummaryInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.7f),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        KocKitText(
            text = text,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            maxLines = 2
        )
    }
}

@Composable
fun ProfileGoalsSection(
    targetRank: String,
    currentRank: String,
    targetProgress: Float,
    targetProgressText: String,
    totalPoints: String,
    pointsPeriod: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ProfileCardStyle.sectionSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitBoldText(
                text = "Hedeflerim",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitSemiText(
                    text = "Tümünü Gör",
                    color = PastelGreen,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = PastelGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(ProfileCardStyle.gridGap)
        ) {
            ProfileTargetGoalCard(
                targetRank = targetRank,
                currentRank = currentRank,
                progress = targetProgress,
                progressText = targetProgressText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            ProfilePointsGoalCard(
                totalPoints = totalPoints,
                period = pointsPeriod,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ProfileTargetGoalCard(
    targetRank: String,
    currentRank: String,
    progress: Float,
    progressText: String,
    modifier: Modifier = Modifier
) {
    ProfileGoalCardShell(
        modifier = modifier,
        icon = Icons.Outlined.TrackChanges,
        iconBg = ProfileCardStyle.orangeLight,
        iconTint = OrangeAccent,
        title = targetRank
    ) {
        KocKitText(
            text = "Mevcut Durum",
            color = TextSecondary,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        KocKitExtraBoldText(
            text = currentRank,
            color = TextPrimary,
            fontSize = 22.sp,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileLinearProgressBar(
            progress = progress,
            color = OrangeAccent,
            height = ProfileCardStyle.progressHeight
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitSemiText(
            text = progressText,
            color = OrangeAccent,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
    }
}

@Composable
private fun ProfilePointsGoalCard(
    totalPoints: String,
    period: String,
    modifier: Modifier = Modifier
) {
    ProfileGoalCardShell(
        modifier = modifier,
        icon = Icons.Outlined.EmojiEvents,
        iconBg = ProfileCardStyle.purpleLight,
        iconTint = LavenderAccent,
        title = "Toplam Puan"
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        KocKitExtraBoldText(
            text = totalPoints,
            color = TextPrimary,
            fontSize = 28.sp,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        KocKitText(
            text = period,
            color = TextSecondary,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
    }
}

@Composable
private fun ProfileGoalCardShell(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(ProfileCardStyle.iconChipSize)
                        .clip(RoundedCornerShape(ProfileCardStyle.iconChipRadius))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                KocKitSemiText(
                    text = title,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun ProfileEducationCard(
    items: List<ProfileInfoRow>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ProfileCardStyle.innerPadding)
        ) {
            ProfileCardTitle(
                icon = Icons.Outlined.School,
                iconBg = PastelGreen.copy(alpha = 0.15f),
                iconTint = PastelGreen,
                title = "Eğitim Bilgileri"
            )
            Spacer(modifier = Modifier.height(10.dp))
            items.forEachIndexed { index, item ->
                ProfileInfoKeyValueRow(label = item.label, value = item.value)
                if (index < items.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = PastelGreen,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(18.dp)
            )
        }
    }
}

@Composable
fun ProfileStudyProgramCard(
    weeklyHours: String,
    weeklyProgress: Float,
    weeklyPercent: String,
    details: List<ProfileStudyDetail>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ProfileCardStyle.innerPadding)
        ) {
            ProfileCardTitle(
                icon = Icons.Outlined.Schedule,
                iconBg = ProfileCardStyle.purpleLight,
                iconTint = LavenderAccent,
                title = "Çalışma Programım"
            )
            Spacer(modifier = Modifier.height(10.dp))
            KocKitText(
                text = "Haftalık Çalışma Süresi",
                color = TextSecondary,
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitExtraBoldText(
                    text = weeklyHours,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )
                KocKitSemiText(
                    text = weeklyPercent,
                    color = LavenderAccent,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileGradientProgressBar(
                progress = weeklyProgress,
                startColor = ProfileCardStyle.purpleGradientStart,
                endColor = ProfileCardStyle.purpleGradientEnd
            )
            Spacer(modifier = Modifier.height(10.dp))
            ProfileStudyDetailsGrid(details = details)
        }
    }
}

@Composable
private fun ProfileStudyDetailsGrid(details: List<ProfileStudyDetail>) {
    val icons = listOf(
        Icons.Outlined.Schedule,
        Icons.Filled.CalendarToday,
        Icons.Outlined.MenuBook,
        Icons.Outlined.Psychology
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFAFAFA))
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ProfileStudyDetailCell(
                icon = icons[0],
                title = details[0].title,
                value = details[0].value,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(
                modifier = Modifier
                    .height(56.dp)
                    .align(Alignment.CenterVertically),
                thickness = 1.dp,
                color = ProfileCardStyle.dividerColor
            )
            ProfileStudyDetailCell(
                icon = icons[1],
                title = details[1].title,
                value = details[1].value,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(thickness = 1.dp, color = ProfileCardStyle.dividerColor)
        Row(modifier = Modifier.fillMaxWidth()) {
            ProfileStudyDetailCell(
                icon = icons[2],
                title = details[2].title,
                value = details[2].value,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(
                modifier = Modifier
                    .height(56.dp)
                    .align(Alignment.CenterVertically),
                thickness = 1.dp,
                color = ProfileCardStyle.dividerColor
            )
            ProfileStudyDetailCell(
                icon = icons[3],
                title = details[3].title,
                value = details[3].value,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProfileStudyDetailCell(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = title,
            color = TextSecondary,
            fontSize = 8.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        KocKitSemiText(
            text = value,
            color = TextPrimary,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfilePrepTimelineCard(
    phase: String,
    phaseSubtitle: String,
    progress: Float,
    progressText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ProfileCardStyle.innerPadding)
        ) {
            ProfileCardTitle(
                icon = Icons.Filled.CalendarToday,
                iconBg = ProfileCardStyle.orangeLight,
                iconTint = OrangeAccent,
                title = "Hazırlık Takvimi"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    KocKitText(
                        text = "Mevcut Evre",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitExtraBoldText(
                        text = phase,
                        color = OrangeAccent,
                        fontSize = 20.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitText(
                        text = phaseSubtitle,
                        color = TextSecondary,
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        maxLines = 2
                    )
                }
                ProfileCircularProgressWithIcon(
                    progress = progress,
                    progressText = progressText,
                    ringColor = OrangeAccent,
                    icon = Icons.Filled.CalendarToday
                )
            }
        }
    }
}

@Composable
fun ProfileUnavailableDaysCard(
    days: List<String>,
    note: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ProfileCardStyle.innerPadding)
        ) {
            ProfileCardTitle(
                icon = Icons.Outlined.EventBusy,
                iconBg = PastelGreen.copy(alpha = 0.15f),
                iconTint = PastelGreen,
                title = "Müsait Olmadığım Günler"
            )
            Spacer(modifier = Modifier.height(10.dp))
            days.forEach { day ->
                ProfileUnavailableDayChip(day = day)
                Spacer(modifier = Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            KocKitText(
                text = note,
                color = TextSecondary.copy(alpha = 0.7f),
                fontSize = 8.sp,
                lineHeight = 10.sp,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun ProfileUnavailableDayChip(day: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = ProfileCardStyle.unavailableChipBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null,
                tint = OrangeAccent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            KocKitSemiText(
                text = day,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun ProfileCardTitle(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(ProfileCardStyle.iconChipSize)
                .clip(RoundedCornerShape(ProfileCardStyle.iconChipRadius))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        KocKitSemiText(
            text = title,
            color = TextPrimary,
            fontSize = 10.sp,
            lineHeight = 15.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun ProfileInfoKeyValueRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        KocKitText(
            text = label,
            color = TextSecondary,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            modifier = Modifier.weight(0.95f),
            maxLines = 2
        )
        Spacer(modifier = Modifier.width(4.dp))
        KocKitSemiText(
            text = value,
            color = TextPrimary,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.05f),
            maxLines = 2
        )
    }
}

@Composable
private fun ProfileLinearProgressBar(
    progress: Float,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(Color(0xFFECECEC))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(height / 2))
                .background(color)
        )
    }
}

@Composable
private fun ProfileGradientProgressBar(
    progress: Float,
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier
) {
    val height = ProfileCardStyle.thickProgressHeight
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(Color(0xFFECECEC))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(height / 2))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
        )
    }
}

@Composable
private fun ProfileCircularProgressWithIcon(
    progress: Float,
    progressText: String,
    ringColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = ringColor,
                trackColor = Color(0xFFE8E8E8),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ringColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        KocKitSemiText(
            text = progressText,
            color = ringColor,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
