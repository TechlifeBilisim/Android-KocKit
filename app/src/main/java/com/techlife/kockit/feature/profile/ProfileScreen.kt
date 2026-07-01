package com.techlife.kockit.feature.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techlife.kockit.core.designsystem.layout.ProfileContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberProfileLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.feature.auth.forgotpassword.ForgotPasswordScreen
import com.techlife.kockit.feature.auth.forgotpassword.ForgotPasswordViewModel
import com.techlife.kockit.feature.profilegoals.ProfileGoalsFlowScreen

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showGoalsFlow by rememberSaveable { mutableStateOf(false) }
    var showChangePassword by rememberSaveable { mutableStateOf(false) }

    if (showChangePassword) {
        val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
        ForgotPasswordScreen(
            viewModel = forgotPasswordViewModel,
            profileFlow = true,
            onNavigateBack = { showChangePassword = false },
            onNavigateToLogin = { showChangePassword = false },
            onNavigateToRegister = {},
            onCompleted = { showChangePassword = false },
            onShowMessage = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
        return
    }

    if (showGoalsFlow) {
        ProfileGoalsFlowScreen(
            onExit = { showGoalsFlow = false },
            onComplete = { showGoalsFlow = false }
        )
        return
    }
    val metrics = rememberProfileLayoutMetrics()

    ProfileContentContainer(metrics = metrics) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground),
            verticalArrangement = Arrangement.spacedBy(metrics.sectionSpacing)
        ) {
            item(key = "top_bar") {
                Spacer(modifier = Modifier.height(metrics.topInset))
                ProfileTopBar(onBackClick = onBackClick)
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
            item(key = "account_settings") {
                ProfileAccountSettingsCard(
                    onPersonalInfoClick = {},
                    onChangePasswordClick = { showChangePassword = true },
                    onGoalsClick = { showGoalsFlow = true },
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
fun ProfileScreenTabletPreview() {
    KocKitTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KocKitTheme {
        ProfileScreen()
    }
}
