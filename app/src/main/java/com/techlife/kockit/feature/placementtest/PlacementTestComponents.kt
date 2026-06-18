package com.techlife.kockit.feature.placementtest

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

@Composable
fun PlacementTestDecorBackground(
    accentSoftColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .clip(CircleShape)
                .background(accentSoftColor.copy(alpha = 0.55f))
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 80.dp)
                .clip(CircleShape)
                .background(accentSoftColor.copy(alpha = 0.4f))
        )
        content()
    }
}

@Composable
fun PlacementTestInfoBackHeader(
    title: String,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(White)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        KocKitExtraBoldText(
            text = title,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeTitle,
            lineHeight = KocKitTextDefaults.lineHeightTitle
        )
    }
}

@Composable
fun PlacementTestInfoHeader(title: String) {
    KocKitExtraBoldText(
        text = title,
        color = TextPrimary,
        fontSize = KocKitTextDefaults.fontSizeTitle,
        lineHeight = KocKitTextDefaults.lineHeightTitle,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PlacementExamTopBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        KocKitBoldText(
            text = title,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlacementSegmentedProgress(
    currentIndex: Int,
    totalQuestions: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalQuestions.coerceAtLeast(1)) { index ->
            val isActive = index <= currentIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (isActive) accentColor else Color(0xFFE8E8E8))
            )
        }
    }
}

@Composable
fun PlacementTimerBadge(
    timerText: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, accentColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Schedule,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp)
        )
        KocKitSemiText(
            text = timerText,
            color = accentColor,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody
        )
    }
}

@Composable
fun PlacementSubjectChip(
    subject: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = accentColor.copy(alpha = 0.15f)
    ) {
        KocKitSemiText(
            text = subject,
            color = accentColor,
            fontSize = KocKitTextDefaults.fontSizeSmall,
            lineHeight = KocKitTextDefaults.lineHeightSmall,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PlacementResultSummaryCard(
    section: PlacementTestSection,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = PlacementTestColors.green,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitBoldText(
                    text = "Sınav Sonuçları",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PlacementTestColors.greenSoft
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = PlacementTestColors.green,
                            modifier = Modifier.size(14.dp)
                        )
                        KocKitSemiText(
                            text = "Tamamlandı",
                            color = PlacementTestColors.green,
                            fontSize = KocKitTextDefaults.fontSizeSmall,
                            lineHeight = KocKitTextDefaults.lineHeightSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                PlacementResultRow(
                    label = "Doğru",
                    value = section.correctCount,
                    valueColor = PlacementTestColors.green,
                    leadingEmoji = "✓"
                )
                PlacementResultRow(
                    label = "Yanlış",
                    value = section.wrongCount,
                    valueColor = PlacementTestColors.wrongRed,
                    leadingEmoji = "✕"
                )
                PlacementResultRow(
                    label = "Boş",
                    value = section.emptyCount,
                    valueColor = PlacementTestColors.emptyGray,
                    leadingEmoji = "○"
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFE8E8E8)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = PlacementTestColors.purple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                KocKitSemiText(
                    text = "Toplam Soru",
                    color = TextSecondary,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody,
                    modifier = Modifier.weight(1f)
                )
                KocKitBoldText(
                    text = section.totalQuestionCount,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
            }
        }
    }
}

@Composable
private fun PlacementResultRow(
    label: String,
    value: String,
    valueColor: Color,
    leadingEmoji: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KocKitBoldText(
            text = leadingEmoji,
            color = valueColor,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            modifier = Modifier.width(24.dp)
        )
        KocKitSemiText(
            text = label,
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            modifier = Modifier.weight(1f)
        )
        KocKitBoldText(
            text = value,
            color = valueColor,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
    }
}

@Composable
fun PlacementCompletionTimeCard(
    completionTime: String,
    averageTime: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PlacementTestColors.greenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = PlacementTestColors.green,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                KocKitText(
                    text = "Tamamlanma Süresi",
                    color = TextSecondary,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall
                )
                KocKitBoldText(
                    text = completionTime,
                    color = PlacementTestColors.green,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
                KocKitText(
                    text = averageTime,
                    color = TextSecondary,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall
                )
            }
        }
    }
}

@Composable
fun PlacementResultAreasGrid(
    strongAreas: List<PlacementResultAreaItem>,
    weakAreas: List<PlacementResultAreaItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PlacementResultAreaColumn(
            title = "Güçlü Olduğun Alanlar",
            items = strongAreas,
            backgroundColor = PlacementTestColors.greenSoft,
            accentColor = PlacementTestColors.green,
            modifier = Modifier.weight(1f)
        )
        PlacementResultAreaColumn(
            title = "Geliştirebileceğin Alanlar",
            items = weakAreas,
            backgroundColor = PlacementTestColors.orangeSoft,
            accentColor = PlacementTestColors.orange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlacementResultAreaColumn(
    title: String,
    items: List<PlacementResultAreaItem>,
    backgroundColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                KocKitSemiText(
                    text = title,
                    color = accentColor,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall,
                    maxLines = 2
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KocKitText(
                        text = item.emoji,
                        fontSize = KocKitTextDefaults.fontSizeBody,
                        lineHeight = KocKitTextDefaults.lineHeightBody
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    KocKitSemiText(
                        text = item.label,
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeSmall,
                        lineHeight = KocKitTextDefaults.lineHeightSmall,
                        maxLines = 2
                    )
                }
            }
            }
        }
    }
}

@Composable
fun PlacementResultActionButtons(
    nextExamButtonText: String?,
    onGoToNextExam: () -> Unit,
    onGoToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (nextExamButtonText != null) {
            OutlinedButton(
                onClick = onGoToNextExam,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, PlacementTestColors.purple),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    containerColor = White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Public,
                    contentDescription = null,
                    tint = PlacementTestColors.purple,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                KocKitSemiText(
                    text = nextExamButtonText,
                    color = PlacementTestColors.purple,
                    fontSize = KocKitTextDefaults.fontSizeSmall,
                    lineHeight = KocKitTextDefaults.lineHeightSmall,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
        androidx.compose.material3.Button(
            onClick = onGoToHome,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = PlacementTestColors.green
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            KocKitSemiText(
                text = "Ana Sayfaya Git",
                color = White,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlacementTestExamHeader(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = TextPrimary
            )
        }
        KocKitBoldText(
            text = title,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun PlacementHeroIcon(
    iconRes: Int,
    accentSoftColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(accentSoftColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(88.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun PlacementStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KocKitText(
                text = label,
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeSmall,
                lineHeight = KocKitTextDefaults.lineHeightSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            KocKitBoldText(
                text = value,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeTitle,
                lineHeight = KocKitTextDefaults.lineHeightTitle
            )
        }
    }
}

@Composable
fun PlacementScopeItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(PastelGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = White
            )
        }
        KocKitSemiText(
            text = text,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
    }
}

@Composable
fun PlacementExamStatusBar(
    timerText: String,
    questionLabel: String,
    currentQuestionIndex: Int,
    totalQuestions: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                KocKitSemiText(
                    text = timerText,
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                )
            }
            KocKitSemiText(
                text = questionLabel,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PlacementExamQuestionProgress(
            currentQuestionIndex = currentQuestionIndex,
            totalQuestions = totalQuestions
        )
    }
}

@Composable
fun PlacementExamQuestionProgress(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalQuestions <= 0) {
        0f
    } else {
        (currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0xFFE8E8E8))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(PlacementTestColors.green)
        )
    }
}

@Composable
fun PlacementAnswerOption(
    label: String,
    text: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) accentColor.copy(alpha = 0.1f) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (isSelected) accentColor else Color(0xFFE8E8E8),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KocKitBoldText(
            text = label,
            color = if (isSelected) accentColor else TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
        Spacer(modifier = Modifier.width(12.dp))
        KocKitSemiText(
            text = text,
            color = TextPrimary,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = White
                )
            }
        }
    }
}

@Composable
fun PlacementResultRing(
    scoreText: String,
    label: String,
    progress: Float,
    ringColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 14.dp.toPx()
            val diameter = size.minDimension - stroke
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
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
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            KocKitExtraBoldText(
                text = scoreText,
                color = TextPrimary,
                fontSize = KocKitTextDefaults.fontSizeHeadline,
                lineHeight = KocKitTextDefaults.lineHeightHeadline,
                textAlign = TextAlign.Center
            )
            KocKitSemiText(
                text = label,
                color = TextSecondary,
                fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                lineHeight = KocKitTextDefaults.lineHeightBodyLarge
            )
        }
    }
}

@Composable
fun PlacementResultStat(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KocKitText(
            text = label,
            color = TextSecondary,
            fontSize = KocKitTextDefaults.fontSizeSmall,
            lineHeight = KocKitTextDefaults.lineHeightSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        KocKitBoldText(
            text = value,
            color = valueColor,
            fontSize = KocKitTextDefaults.fontSizeTitle,
            lineHeight = KocKitTextDefaults.lineHeightTitle
        )
    }
}
