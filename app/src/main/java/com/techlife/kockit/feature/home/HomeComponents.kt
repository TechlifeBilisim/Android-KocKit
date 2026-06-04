package com.techlife.kockit.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun HomeHeader(
    userName: String,
    notificationCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            KocKitExtraBoldText(
                text = "Merhaba, $userName! 👋",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
            Spacer(modifier = Modifier.height(4.dp))
            KocKitText(
                text = "Bugün harika bir gün!",
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = TextPrimary
            )
            Box {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
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
                Column(modifier = Modifier.weight(1f)) {
                    KocKitBoldText(
                        text = "Performans Analizi",
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitText(
                        text = "Son analiz: Bugün",
                        color = TextSecondary,
                        fontSize = KocKitTextDefaults.fontSizeBody,
                        lineHeight = KocKitTextDefaults.lineHeightBody
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HomePerformanceInsightRow(
                label = "En Güçlü Ders",
                subject = "Matematik",
                percent = "%78",
                accentColor = HomePerformanceCardStyle.green,
                accentLightColor = HomePerformanceCardStyle.greenLight,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = HomePerformanceCardStyle.green,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                thickness = 1.dp,
                color = HomePerformanceCardStyle.border
            )

            HomePerformanceInsightRow(
                label = "Gelişim Alanı",
                subject = "Fizik",
                percent = "%42",
                accentColor = HomePerformanceCardStyle.orange,
                accentLightColor = HomePerformanceCardStyle.orangeLight,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.TrackChanges,
                        contentDescription = null,
                        tint = HomePerformanceCardStyle.orange,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    tint = HomePerformanceCardStyle.green,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitText(
                    text = "Son 7 günde ",
                    color = HomePerformanceCardStyle.summaryMuted,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
                KocKitBoldText(
                    text = "+%6",
                    color = HomePerformanceCardStyle.green,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
                KocKitText(
                    text = " gelişim",
                    color = HomePerformanceCardStyle.summaryMuted,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = HomePerformanceCardStyle.green,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                KocKitBoldText(
                    text = "Detaylı Analizi Gör",
                    color = HomePerformanceCardStyle.green,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = HomePerformanceCardStyle.green,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                        .size(20.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun HomePerformanceInsightRow(
    label: String,
    subject: String,
    percent: String,
    accentColor: Color,
    accentLightColor: Color,
    leadingIcon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(accentLightColor),
            contentAlignment = Alignment.Center
        ) {
            leadingIcon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            KocKitSemiText(
                text = label,
                color = accentColor,
                fontSize = KocKitTextDefaults.fontSizeBody,
                lineHeight = KocKitTextDefaults.lineHeightBody
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitBoldText(
                    text = subject,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = accentLightColor
        ) {
            KocKitBoldText(
                text = percent,
                color = accentColor,
                fontSize = KocKitTextDefaults.fontSizeBody,
                lineHeight = KocKitTextDefaults.lineHeightBody,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun HomeProgressCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            KocKitBoldText(
                text = "Genel İlerleme",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(10.dp))
            HomeCircularProgress(
                progress = 0.68f,
                percentText = "%68",
                label = "İlerleme",
                ringColor = LavenderAccent,
                ringSize = 84.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            KocKitText(
                text = "Geçen haftaya göre +%8 artış",
                color = LavenderAccent,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                textAlign = TextAlign.Center,
                maxLines = 3
            )
        }
    }
}

@Composable
fun HomePointsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF3D6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFE8A830),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            KocKitBoldText(
                text = "Toplam Puan",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(10.dp))
            KocKitExtraBoldText(
                text = "850",
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
            KocKitText(
                text = "Toplam Puan",
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = OrangeAccent.copy(alpha = 0.12f)
            ) {
                KocKitSemiText(
                    text = "İlk %10 Sıralaman",
                    color = OrangeAccent,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun HomeCircularProgress(
    progress: Float,
    percentText: String,
    label: String,
    ringColor: Color,
    ringSize: Dp = 120.dp
) {
    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(ringSize)) {
            val stroke = 10.dp.toPx()
            val diameter = this.size.minDimension - stroke
            val topLeft = Offset((this.size.width - diameter) / 2f, (this.size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)
            drawArc(
                color = Color(0xFFE8E8E8),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KocKitExtraBoldText(
                text = percentText,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
            KocKitText(
                text = label,
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.TrackChanges,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitBoldText(
                    text = "Bugünkü Önceliğin",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp)
                ) {
                    KocKitSemiText(
                        text = "Planını Gör",
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
                modifier = Modifier.size(56.dp),
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
