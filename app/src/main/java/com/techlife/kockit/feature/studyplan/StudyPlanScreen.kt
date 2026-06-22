package com.techlife.kockit.feature.studyplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun StudyPlanScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var days by remember { mutableStateOf(StudyPlanFakeData.initialDays) }
    var studyPeriodMinutes by remember { mutableIntStateOf(StudyPlanFakeData.INITIAL_STUDY_PERIOD_MINUTES) }
    var paragraphMinutes by remember { mutableIntStateOf(StudyPlanFakeData.INITIAL_PARAGRAPH_MINUTES) }
    val weeklyHours = days.sumOf { it.hours }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "top_bar") {
            Spacer(modifier = Modifier.height(8.dp))
            StudyPlanTopBar(onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(4.dp))
        }
        item(key = "weekly_summary") {
            StudyPlanWeeklySummaryCard(weeklyHours = weeklyHours)
        }
        item(key = "days_grid") {
            days.chunked(3).forEachIndexed { rowIndex, rowDays ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowDays.forEachIndexed { colIndex, day ->
                        val dayIndex = rowIndex * 3 + colIndex
                        StudyPlanDayCard(
                            day = day,
                            onHoursChange = { newHours ->
                                days = days.mapIndexed { index, item ->
                                    if (index == dayIndex) item.copy(hours = newHours) else item
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(3 - rowDays.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                if (rowIndex < days.lastIndex / 3) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        item(key = "study_period") {
            StudyPlanSettingCard(
                title = "Günlük Çalışma Periyodu",
                subtitle = "Çalışma Periyodu Belirleyin",
                value = studyPeriodMinutes,
                onValueChange = { studyPeriodMinutes = it },
                pickerTitle = "Günlük Çalışma Periyodu (dk)",
                range = 15..180,
                suffix = "dk"
            )
        }
        item(key = "paragraph") {
            StudyPlanSettingCard(
                title = "Her gün kaç dakika paragraf soruları çözeceksiniz?",
                subtitle = "Günlük Paragraf Süresi",
                value = paragraphMinutes,
                onValueChange = { paragraphMinutes = it },
                pickerTitle = "Günlük Paragraf Süresi (dk)",
                range = 0..120,
                suffix = "dk"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudyPlanScreenPreview() {
    KocKitTheme {
        StudyPlanScreen(onBackClick = {})
    }
}
