package com.techlife.kockit.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "top_bar") {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileTopBar(
                notificationCount = ProfileFakeData.NOTIFICATION_COUNT,
                onBackClick = onBackClick
            )
        }
        item(key = "header") {
            ProfileHeaderSection()
        }
        item(key = "summary") {
            ProfileSummaryCard(
                fullName = ProfileFakeData.FULL_NAME,
                grade = ProfileFakeData.GRADE,
                examType = ProfileFakeData.EXAM_TYPE,
                location = ProfileFakeData.LOCATION,
                school = ProfileFakeData.SCHOOL,
                levelLabel = ProfileFakeData.LEVEL_LABEL
            )
        }
        item(key = "goals") {
            ProfileGoalsSection(
                targetRank = ProfileFakeData.TARGET_RANK,
                currentRank = ProfileFakeData.CURRENT_RANK,
                targetProgress = ProfileFakeData.TARGET_PROGRESS,
                targetProgressText = ProfileFakeData.TARGET_PROGRESS_TEXT,
                totalPoints = ProfileFakeData.TOTAL_POINTS,
                pointsPeriod = ProfileFakeData.POINTS_PERIOD
            )
        }
        item(key = "study_program") {
            ProfileStudyProgramCard(
                weeklyHours = ProfileFakeData.WEEKLY_STUDY_HOURS,
                weeklyProgress = ProfileFakeData.WEEKLY_STUDY_PROGRESS,
                weeklyPercent = ProfileFakeData.WEEKLY_STUDY_PERCENT,
                details = ProfileFakeData.studyDetails,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item(key = "prep_unavailable_row") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfilePrepTimelineCard(
                    phase = ProfileFakeData.PREP_PHASE,
                    phaseSubtitle = ProfileFakeData.PREP_PHASE_SUBTITLE,
                    progress = ProfileFakeData.PREP_PROGRESS,
                    progressText = ProfileFakeData.PREP_PROGRESS_TEXT,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                ProfileUnavailableDaysCard(
                    days = ProfileFakeData.unavailableDays,
                    note = ProfileFakeData.UNAVAILABLE_DAYS_NOTE,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KocKitTheme {
        ProfileScreen()
    }
}
