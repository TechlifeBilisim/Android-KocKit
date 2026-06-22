package com.techlife.kockit.feature.studyplan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitNumberPickerField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.PrimaryTeal
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

private object StudyPlanStyle {
    val dayCardBg = PastelGreen.copy(alpha = 0.12f)
    val settingCardBg = PastelGreen.copy(alpha = 0.18f)
}

@Composable
fun StudyPlanTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = White,
            shadowElevation = 2.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        KocKitBoldText(
            text = "Çalışma Planı",
            color = TextPrimary,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun StudyPlanWeeklySummaryCard(
    weeklyHours: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitSemiText(
                text = "Haftalık Ders Süresi",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            KocKitExtraBoldText(
                text = "$weeklyHours Saat",
                color = TextPrimary,
                fontSize = 16.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun StudyPlanDayCard(
    day: StudyPlanDay,
    onHoursChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudyPlanStyle.dayCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KocKitSemiText(
                text = day.name,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                maxLines = 1
            )
            KocKitNumberPickerField(
                value = day.hours,
                onValueChange = onHoursChange,
                range = 0..24,
                pickerTitle = "${day.name} - Saat",
                compact = true
            )
        }
    }
}

@Composable
fun StudyPlanSettingCard(
    title: String,
    subtitle: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    pickerTitle: String,
    range: IntRange,
    suffix: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudyPlanStyle.settingCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            KocKitBoldText(
                text = title,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitText(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.size(12.dp))
                KocKitNumberPickerField(
                    value = value,
                    onValueChange = onValueChange,
                    range = range,
                    pickerTitle = pickerTitle,
                    suffix = suffix,
                    compact = false
                )
            }
        }
    }
}
