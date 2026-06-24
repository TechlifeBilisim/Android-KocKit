package com.techlife.kockit.feature.studyplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.techlife.kockit.core.designsystem.layout.StudyPlanContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberStudyPlanLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun StudyPlanScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = rememberStudyPlanLayoutMetrics()
    var days by remember { mutableStateOf(StudyPlanFakeData.initialDays) }
    var sessionMinutes by remember { mutableIntStateOf(StudyPlanFakeData.INITIAL_SESSION_MINUTES) }
    var paragraphMinutes by remember { mutableIntStateOf(StudyPlanFakeData.INITIAL_PARAGRAPH_MINUTES) }
    var problemMinutes by remember { mutableIntStateOf(StudyPlanFakeData.INITIAL_PROBLEM_MINUTES) }
    var revisionDay by remember { mutableStateOf(StudyPlanFakeData.INITIAL_REVISION_DAY) }
    var unavailableDays by remember { mutableStateOf(StudyPlanFakeData.initialUnavailableDays) }
    var specialDates by remember { mutableStateOf(StudyPlanFakeData.initialSpecialDates) }
    var showAddSpecialDateSheet by remember { mutableStateOf(false) }

    val totalHours = days.sumOf { it.hours }

    if (showAddSpecialDateSheet) {
        StudyPlanAddSpecialDateSheet(
            onDismiss = { showAddSpecialDateSheet = false },
            onAdd = { newDate ->
                specialDates = specialDates + newDate
                showAddSpecialDateSheet = false
            }
        )
    }

    StudyPlanContentContainer(metrics = metrics, modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            item(key = "header") {
                Spacer(modifier = Modifier.height(metrics.topInset))
                StudyPlanHeader(
                    onBackClick = onBackClick,
                    onAutoPlanClick = { },
                    description = StudyPlanFakeData.DESCRIPTION
                )
                Spacer(modifier = Modifier.height(metrics.sectionSpacing + 8.dp))
            }

            item(key = "days") {
                StudyPlanDaysSection(
                    days = days,
                    totalHours = totalHours,
                    onHoursChange = { index, newHours ->
                        days = days.mapIndexed { i, item ->
                            if (i == index) item.copy(hours = newHours) else item
                        }
                    }
                )
                Spacer(modifier = Modifier.height(metrics.sectionSpacing))
            }

            item(key = "parameters") {
                StudyPlanParametersSection(
                    sessionMinutes = sessionMinutes,
                    onSessionChange = { sessionMinutes = it },
                    paragraphMinutes = paragraphMinutes,
                    onParagraphChange = { paragraphMinutes = it },
                    problemMinutes = problemMinutes,
                    onProblemChange = { problemMinutes = it },
                    revisionDay = revisionDay,
                    onRevisionDayChange = { revisionDay = it },
                    revisionOptions = StudyPlanFakeData.revisionDayOptions
                )
                Spacer(modifier = Modifier.height(metrics.sectionSpacing))
            }

            item(key = "unavailable") {
                StudyPlanUnavailableSection(
                    weekDayOptions = StudyPlanFakeData.weekDayOptions,
                    selectedUnavailableDays = unavailableDays,
                    specialDates = specialDates,
                    onUnavailableDayToggle = { shortName ->
                        unavailableDays = if (shortName in unavailableDays) {
                            unavailableDays - shortName
                        } else {
                            unavailableDays + shortName
                        }
                    },
                    onRemoveSpecialDate = { id ->
                        specialDates = specialDates.filterNot { it.id == id }
                    },
                    onAddSpecialDateClick = { showAddSpecialDateSheet = true }
                )
                Spacer(modifier = Modifier.height(metrics.sectionSpacing + 4.dp))
            }

            item(key = "save") {
                StudyPlanSaveButton(onClick = { })
                Spacer(modifier = Modifier.height(metrics.sectionSpacing))
            }

            item(key = "info") {
                StudyPlanInfoNote(text = StudyPlanFakeData.SAVE_INFO_NOTE)
                Spacer(modifier = Modifier.height(metrics.topInset + 8.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
private fun StudyPlanScreenTabletPreview() {
    KocKitTheme {
        StudyPlanScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun StudyPlanScreenPreview() {
    KocKitTheme {
        StudyPlanScreen(onBackClick = {})
    }
}
