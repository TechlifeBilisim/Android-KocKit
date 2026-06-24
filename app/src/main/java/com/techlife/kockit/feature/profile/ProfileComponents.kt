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
import com.techlife.kockit.core.designsystem.layout.LocalProfileLayoutMetrics
import com.techlife.kockit.core.designsystem.layout.ProfileLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CardShape
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

private object ProfileCardStyle {
    val levelBadgeGreen = Color(0xFF4DB6AC)
    val levelBadgeBg = Color(0xFFE8F6F3)
    val orangeLight = Color(0xFFFFF0E6)
    val purpleLight = Color(0xFFF0ECFA)
    val purpleGradientStart = Color(0xFF9B8FD9)
    val purpleGradientEnd = Color(0xFFD4CCF0)
    val unavailableChipBg = Color(0xFFFFF5EE)
    val dividerColor = Color(0xFFF0F0F0)
}

@Composable
private fun profileMetrics() = LocalProfileLayoutMetrics.current

@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = profileMetrics()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(metrics.backButtonSize)
                .clip(CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                modifier = Modifier.size(metrics.backIconSize),
                tint = TextPrimary
            )
        }
    }
}

@Composable
fun ProfileHeaderSection(modifier: Modifier = Modifier) {
    val metrics = profileMetrics()
    Column(modifier = modifier.fillMaxWidth()) {
        KocKitExtraBoldText(
            text = "Profilim",
            color = TextPrimary,
            fontSize = metrics.headerTitleSize,
            lineHeight = metrics.headerTitleLineHeight
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = "Hedeflerine ulaşmak için bilgilerini güncel tut.",
            color = TextSecondary,
            fontSize = metrics.headerSubtitleSize,
            lineHeight = metrics.headerSubtitleLineHeight
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
    val metrics = profileMetrics()
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(metrics.cardInnerPadding),
                verticalAlignment = Alignment.Top
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(metrics.profileAvatarSize)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F0FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Color(0xFF90A4C8),
                            modifier = Modifier.size(metrics.profileAvatarIconSize)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .size(metrics.cameraBadgeSize)
                            .clip(CircleShape)
                            .background(PastelGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Fotoğraf düzenle",
                            tint = White,
                            modifier = Modifier.size(metrics.cameraBadgeIconSize)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (metrics.isExpanded) 88.dp else 72.dp)
                ) {
                    KocKitBoldText(
                        text = fullName,
                        color = TextPrimary,
                        fontSize = metrics.cardTitleSize,
                        lineHeight = metrics.cardTitleLineHeight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.School, grade, metrics)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.Description, examType, metrics)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.LocationOn, location, metrics)
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileSummaryInfoRow(Icons.Outlined.AccountBalance, school, metrics)
                }
            }
            ProfileLevelBadge(
                levelLabel = levelLabel,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = metrics.cardInnerPadding,
                        end = metrics.cardInnerPadding
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
    val metrics = profileMetrics()
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
                fontSize = metrics.microCaptionSize,
                lineHeight = metrics.microCaptionLineHeight
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
                        modifier = Modifier.size(metrics.infoIconSize)
                    )
                    KocKitSemiText(
                        text = levelLabel,
                        color = ProfileCardStyle.levelBadgeGreen,
                        fontSize = metrics.smallCaptionSize,
                        lineHeight = metrics.smallCaptionLineHeight
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSummaryInfoRow(
    icon: ImageVector,
    text: String,
    metrics: ProfileLayoutMetrics
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.7f),
            modifier = Modifier.size(metrics.infoIconSize)
        )
        Spacer(modifier = Modifier.width(6.dp))
        KocKitText(
            text = text,
            color = TextSecondary,
            fontSize = metrics.smallCaptionSize,
            lineHeight = metrics.smallCaptionLineHeight,
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
    val metrics = profileMetrics()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(metrics.cardSectionSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitBoldText(
                text = "Hedeflerim",
                color = TextPrimary,
                fontSize = metrics.cardTitleSize,
                lineHeight = metrics.cardTitleLineHeight
            )
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitSemiText(
                    text = "Tümünü Gör",
                    color = PastelGreen,
                    fontSize = metrics.cardCaptionSize,
                    lineHeight = metrics.cardCaptionLineHeight
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = PastelGreen,
                    modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(metrics.gridGap)
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
    val metrics = profileMetrics()
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
            fontSize = metrics.smallCaptionSize,
            lineHeight = metrics.smallCaptionLineHeight
        )
        Spacer(modifier = Modifier.height(2.dp))
        KocKitExtraBoldText(
            text = currentRank,
            color = TextPrimary,
            fontSize = metrics.statValueSize,
            lineHeight = metrics.statValueLineHeight
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileLinearProgressBar(
            progress = progress,
            color = OrangeAccent,
            height = metrics.progressHeight
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitSemiText(
            text = progressText,
            color = OrangeAccent,
            fontSize = metrics.smallCaptionSize,
            lineHeight = metrics.smallCaptionLineHeight
        )
    }
}

@Composable
private fun ProfilePointsGoalCard(
    totalPoints: String,
    period: String,
    modifier: Modifier = Modifier
) {
    val metrics = profileMetrics()
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
            fontSize = metrics.largeStatValueSize,
            lineHeight = metrics.largeStatValueLineHeight
        )
        Spacer(modifier = Modifier.weight(1f))
        KocKitSemiText(
            text = period,
            color = TextSecondary,
            fontSize = metrics.cardBodySize,
            lineHeight = metrics.cardBodyLineHeight
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
    val metrics = profileMetrics()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (metrics.isExpanded) 14.dp else 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(metrics.iconChipSize)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(metrics.iconChipIconSize)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                KocKitSemiText(
                    text = title,
                    color = TextPrimary,
                    fontSize = metrics.smallCaptionSize,
                    lineHeight = metrics.smallCaptionLineHeight,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
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
    val metrics = profileMetrics()
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(metrics.cardInnerPadding)
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
                fontSize = metrics.smallCaptionSize,
                lineHeight = metrics.smallCaptionLineHeight
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
                    fontSize = if (metrics.isExpanded) 22.sp else 18.sp,
                    lineHeight = if (metrics.isExpanded) 26.sp else 22.sp
                )
                KocKitSemiText(
                    text = weeklyPercent,
                    color = LavenderAccent,
                    fontSize = metrics.cardCaptionSize,
                    lineHeight = metrics.cardCaptionLineHeight
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileGradientProgressBar(progress = weeklyProgress)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileStudyDetailsGrid(details = details)
        }
    }
}

@Composable
private fun ProfileStudyDetailsGrid(details: List<ProfileStudyDetail>) {
    val metrics = profileMetrics()
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
                    .height(metrics.studyDetailCellHeight)
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
                    .height(metrics.studyDetailCellHeight)
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
    val metrics = profileMetrics()
    Column(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(metrics.infoIconSize)
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = title,
            color = TextSecondary,
            fontSize = metrics.microCaptionSize,
            lineHeight = metrics.microCaptionLineHeight,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        KocKitSemiText(
            text = value,
            color = TextPrimary,
            fontSize = metrics.smallCaptionSize,
            lineHeight = metrics.smallCaptionLineHeight,
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
    val metrics = profileMetrics()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(metrics.cardInnerPadding)
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
                        fontSize = metrics.smallCaptionSize,
                        lineHeight = metrics.smallCaptionLineHeight
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitExtraBoldText(
                        text = phase,
                        color = OrangeAccent,
                        fontSize = if (metrics.isExpanded) 24.sp else 20.sp,
                        lineHeight = if (metrics.isExpanded) 28.sp else 24.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitText(
                        text = phaseSubtitle,
                        color = TextSecondary,
                        fontSize = metrics.microCaptionSize,
                        lineHeight = metrics.microCaptionLineHeight,
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
    val metrics = profileMetrics()
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(metrics.cardInnerPadding)
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
                fontSize = metrics.microCaptionSize,
                lineHeight = metrics.microCaptionLineHeight,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun ProfileUnavailableDayChip(day: String) {
    val metrics = profileMetrics()
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
                modifier = Modifier.size(metrics.infoIconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            KocKitSemiText(
                text = day,
                color = TextPrimary,
                fontSize = metrics.smallCaptionSize,
                lineHeight = metrics.smallCaptionLineHeight
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
    val metrics = profileMetrics()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(metrics.iconChipSize)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(metrics.iconChipIconSize)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        KocKitSemiText(
            text = title,
            color = TextPrimary,
            fontSize = metrics.smallCaptionSize,
            lineHeight = metrics.smallCaptionLineHeight,
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
    modifier: Modifier = Modifier
) {
    val metrics = profileMetrics()
    val height = metrics.thickProgressHeight
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
                        colors = listOf(
                            ProfileCardStyle.purpleGradientStart,
                            ProfileCardStyle.purpleGradientEnd
                        )
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
    val metrics = profileMetrics()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(metrics.progressRingSize),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = ringColor,
                trackColor = Color(0xFFE8E8E8),
                strokeWidth = if (metrics.isExpanded) 5.dp else 4.dp,
                strokeCap = StrokeCap.Round
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ringColor,
                modifier = Modifier.size(metrics.progressRingIconSize)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        KocKitSemiText(
            text = progressText,
            color = ringColor,
            fontSize = metrics.microCaptionSize,
            lineHeight = metrics.microCaptionLineHeight,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
